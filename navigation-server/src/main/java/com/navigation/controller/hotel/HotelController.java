package com.navigation.controller.hotel;

import com.navigation.entity.Hotel;
import com.navigation.entity.Hotel;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Resource
    private HotelService hotelService;

    @PostMapping("/save")
    public Result<Void> saveHotel(@RequestBody Hotel hotel){

        return hotelService.saveHotel(hotel);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Hotel hotel){

        return hotelService.update(hotel);
    }

    @DeleteMapping("/batchDelete")
    public Result<Void> batchDelete(@RequestParam List<Integer> ids){

        return hotelService.batchDelete(ids);
    }

    @GetMapping("/query")
    public PageResult queryHotel(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "5") Integer pageSize){
        //两个参数分别指：从第几页开始查，每页的个数有多少
        return hotelService.queryHotel(page,pageSize);
    }

    @GetMapping("/queryById")
    public Hotel queryHotelById(Integer id){
        return hotelService.queryHotelById(id);
    }

}