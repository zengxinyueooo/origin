package com.navigation.controller.scenic;

import com.navigation.dto.ScenicQueryDto;
import com.navigation.entity.Scenic;
import com.navigation.result.PageResult;
import com.navigation.result.Result;

import com.navigation.service.ScenicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/scenic")
public class ScenicController {

    @Resource
    private ScenicService scenicService;

    @PostMapping("/save")
    public Result<Void> saveScenic(@RequestBody Scenic scenic){

        return scenicService.saveScenic(scenic);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Scenic scenic){
        return scenicService.update(scenic);
    }

    @DeleteMapping("/batchDelete")
    public Result<Void> batchDelete(@RequestParam List<Integer> ids){
        return scenicService.batchDelete(ids);
    }

    @GetMapping("/query")
    public PageResult queryScenic(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "5") Integer pageSize){
        //两个参数分别指：从第几页开始查，每页的个数有多少
        return scenicService.queryScenic(page,pageSize);
    }

    @GetMapping("/queryById")
    public Result<Scenic> queryScenicById(Integer id){
        return scenicService.queryScenicById(id);
    }





}
