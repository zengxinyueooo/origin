package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Scenic;
import com.navigation.mapper.ScenicMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.ScenicService;
import com.navigation.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {


    @Resource
    private ScenicMapper scenicMapper;

    @Autowired
    private Validator validator;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Void> saveScenic(Scenic scenic) {
        // 参数校验
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法保存景点信息");
            return Result.error("传入的景点信息为空");
        }

        List<String> missingFields = new ArrayList<>();
        // 假设Scenic类中有name、description等必填字段，这里以name为例
        if (scenic.getScenicName() == null || scenic.getScenicName().trim().isEmpty()) {
            missingFields.add("scenic_name");
        }
        if (scenic.getScenicDescription() == null || scenic.getScenicDescription().trim().isEmpty()) {
            missingFields.add("scenic_description");
        }
        if (scenic.getLocation() == null || scenic.getLocation().trim().isEmpty()) {
            missingFields.add("scenic_location");
        }
        if (scenic.getMaxCapacity() == null || scenic.getMaxCapacity().toString().isEmpty()) {
            missingFields.add("scenic_location");
        }
        if (scenic.getScenicCover() == null || scenic.getScenicCover().trim().isEmpty()) {
            missingFields.add("scenic_cover");
        }
        if (scenic.getOpenEndTime() == null) {
            missingFields.add("open_end_time");
        }
        if (scenic.getOpenStartTime() == null) {
            missingFields.add("open_start_time");
        }
        // 可根据实际情况继续添加对其他必填字段的检查，如description等
        if (!missingFields.isEmpty()) {
            String fieldsString = String.join(", ", missingFields);
            return Result.error("以下必填参数未传入: " + fieldsString);
        }

        try {
            scenic.setScenicStatus(1);
            scenic.setCreateTime(LocalDateTime.now());
            scenic.setUpdateTime(LocalDateTime.now());
            // 获取自增后的scenicId并设置到Scenic对象中
            long newScenicId = getIncrementedScenicId();
            scenic.setId((int) newScenicId);
            // 构建Redis的key
            String key = "scenic:" + scenic.getId() + ":" + scenic.getScenicName();
            // 将Scenic对象转换为JSON字符串
            String scenicJson = JsonUtils.toJson(scenic);
            stringRedisTemplate.opsForValue().set(key, scenicJson);
            return Result.success();
        } catch (Exception e) {
            log.error("保存景点信息时出现异常", e);
            return Result.error("保存景点信息失败，请稍后重试");
        }
    }

    // 假设获取自增ScenicId的方法，需根据实际业务逻辑实现，这里仅为示例
    private long getIncrementedScenicId() {
        // 这里可以是从数据库序列、Redis自增键等方式获取自增ID
        // 示例从Redis获取自增键值
        String idKey = "scenic_id_increment";
        return stringRedisTemplate.opsForValue().increment(idKey);
    }

   /* public Result<Void> saveScenic(Scenic scenic) {
        // 参数校验
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法保存景点信息");
            return Result.error("传入的景点信息为空");
        }

        // 使用Validator校验Scenic对象的其他必填字段
        Set<ConstraintViolation<Scenic>> violations = validator.validate(scenic);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下必填参数未传入: ");
            for (ConstraintViolation<Scenic> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
        }

        try {
            scenic.setScenicStatus(1);
            scenic.setCreateTime(LocalDateTime.now());
            scenic.setUpdateTime(LocalDateTime.now());
            scenicMapper.saveScenic(scenic);
            return Result.success();
        } catch (Exception e) {
            log.error("保存景点信息时出现异常", e);
            // 这里如果开启了事务，异常会触发事务回滚
            return Result.error("保存景点信息失败，请稍后重试");
        }
    }*/

    @Override
    public Result<Void> update(Scenic scenic) {
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法更新景点信息");
            return Result.error("传入的景点信息为空");
        }

        try {
            // 查找包含该scenic.getId()的所有key（假设Redis存储中有相关关联方式，这里以scenicId构建pattern示例）
            Set<String> keys = stringRedisTemplate.keys("scenic:" + scenic.getId() + ":*");
            if (keys.isEmpty()) {
                log.error("未找到景点id为 {} 的记录", scenic.getId());
                return Result.error("未找到对应景点记录");
            }

            // 假设只有一条记录匹配（实际可能需更复杂逻辑处理多条匹配情况）
            String oldKey = keys.iterator().next();
            // 从Redis获取原数据
            String scenicJson = stringRedisTemplate.opsForValue().get(oldKey);
            Scenic originalScenic = null;
            if (scenicJson != null) {
                try {
                    originalScenic = JsonUtils.fromJson(scenicJson, Scenic.class);
                    // 更新需要修改的字段，这里对各字段进行判断更新
                    if (scenic.getScenicName() != null) {
                        originalScenic.setScenicName(scenic.getScenicName());
                    }
                    if (scenic.getScenicCover() != null) {
                        originalScenic.setScenicCover(scenic.getScenicCover());
                    }
                    if (scenic.getLocation() != null) {
                        originalScenic.setLocation(scenic.getLocation());
                    }
                    if (scenic.getScenicDescription() != null) {
                        originalScenic.setScenicDescription(scenic.getScenicDescription());
                    }
                    if (scenic.getMaxCapacity() != null) {
                        originalScenic.setMaxCapacity(scenic.getMaxCapacity());
                    }
                    if (scenic.getOpenStartTime() != null) {
                        originalScenic.setOpenStartTime(scenic.getOpenStartTime());
                    }
                    if (scenic.getOpenEndTime() != null) {
                        originalScenic.setOpenEndTime(scenic.getOpenEndTime());
                    }
                    if (scenic.getScenicStatus() != null) {
                        originalScenic.setScenicStatus(scenic.getScenicStatus());
                    }
                    originalScenic.setUpdateTime(LocalDateTime.now());
                } catch (Exception e) {
                    log.error("将Redis获取的JSON数据反序列化为Scenic对象时出错，错误信息: {}", e.getMessage());
                    return Result.error("景点数据反序列化失败");
                }
            }

            if (originalScenic != null) {
                // 将更新后的对象转换为JSON字符串并存Redis
                String updatedScenicJson = JsonUtils.toJson(originalScenic);
                // 生成新的键名
                String newKey = "scenic:" + scenic.getId() + ":" + originalScenic.getScenicName();
                stringRedisTemplate.opsForValue().set(newKey, updatedScenicJson);
                // 删除旧键
                stringRedisTemplate.delete(oldKey);
                return Result.success();
            } else {
                log.error("景点记录在Redis中存在问题");
                return Result.error("景点记录处理异常");
            }
        } catch (Exception e) {
            log.error("更新景点信息时出现异常", e);
            return Result.error("更新景点信息时出现异常: " + e.getMessage());
        }
    }

    /*public Result<Void> update(Scenic scenic) {
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法更新景点信息");
            return Result.error("传入的景点信息为空");
        }

        try {
            // 检查scenicId是否存在
            Integer scenicId = scenic.getId();
            int count = scenicMapper.countScenicById(scenicId);
            if (count == 0) {
                log.error("景区id为 {} 的景区记录不存在", scenicId);
                return Result.error("景区id为 " + scenicId + " 的景区记录不存在");
            }

            scenic.setUpdateTime(LocalDateTime.now());
            scenicMapper.update(scenic);
            return Result.success();
        } catch (Exception e) {
            log.error("更新景点信息时出现异常", e);
            throw new RuntimeException(e);
        }
    }*/

    @Override
    @Transactional  // 添加事务管理（确保批量删除原子性）
    public Result<Void> batchDelete(List<Integer> ids) {
        // 检查传入的ID列表是否为空
        if (ids == null || ids.isEmpty()) {
            return Result.error("删除的ID列表不能为空");
        }

        try {
            // 用于存储不存在的ID
            List<Integer> nonExistingIds = new ArrayList<>();

            // 检查传入的每个ID是否存在于Redis中
            for (Integer id : ids) {
                // 构建匹配键的模式
                String pattern = "scenic:" + id + ":*";
                Set<String> keys = stringRedisTemplate.keys(pattern);
                if (keys.isEmpty()) {
                    nonExistingIds.add(id);
                }
            }

            // 如果有不存在的ID，返回包含所有不存在ID的错误信息
            if (!nonExistingIds.isEmpty()) {
                String idsString = nonExistingIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                return Result.error("ID为 " + idsString + " 的景点记录不存在，删除失败");
            }

            // 执行批量删除操作
            for (Integer id : ids) {
                String pattern = "scenic:" + id + ":*";
                Set<String> keys = stringRedisTemplate.keys(pattern);
                for (String key : keys) {
                    stringRedisTemplate.delete(key);
                }
            }
            return Result.success();
        } catch (Exception e) {
            // 记录详细的异常日志，实际应用中建议使用日志框架，如Logback或Log4j
            log.error("批量删除景点过程中出现异常", e);
            // 返回包含具体异常信息的错误结果
            return Result.error("删除过程中出现异常: " + e.getMessage());
        }
    }

    @Override
    public PageResult queryScenic(Integer pageNum, Integer pageSize) {
        String pattern = "scenic:*:*";
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(pageSize)
                .build();

        List<Scenic> scenicList = new ArrayList<>();
        long total = 0;

        try (Cursor<String> cursor = stringRedisTemplate.scan(scanOptions)) {
            int index = 0;
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = startIndex + pageSize;
            while (cursor.hasNext()) {
                String key = cursor.next();
                total++;
                if (index >= startIndex && index < endIndex) {
                    String scenicJson = stringRedisTemplate.opsForValue().get(key);
                    if (scenicJson != null) {
                        Scenic scenic = JsonUtils.fromJson(scenicJson, Scenic.class);
                        scenicList.add(scenic);
                    }
                }
                index++;
            }
        } catch (Exception e) {
            log.error("扫描Redis键时出错: {}", e.getMessage());
        }

        // 按scenicId排序
        scenicList.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));

        return new PageResult(total, scenicList);
    }

    @Override
    public Result<Scenic> queryScenicById(Integer id) {
        if (id == null) {
            log.error("传入的景点ID为空");
            return Result.error("传入的景点ID为空");
        }

        // 构建匹配键的模式
        String pattern = "scenic:" + id + ":*";
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys.isEmpty()) {
            return Result.error("景点Id不存在");
        }

        try {
            // 假设只有一个键匹配（实际可能需处理多个匹配情况）
            String key = keys.iterator().next();
            String scenicJson = stringRedisTemplate.opsForValue().get(key);
            Scenic scenic = JsonUtils.fromJson(scenicJson, Scenic.class);
            return Result.success(scenic);
        } catch (Exception e) {
            log.error("将Redis获取的JSON数据反序列化为Scenic对象时出错，错误信息: {}", e.getMessage());
            return Result.error("查询景点信息时出现异常: " + e.getMessage());
        }
    }


}
