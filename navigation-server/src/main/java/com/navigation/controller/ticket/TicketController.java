package com.navigation.controller.ticket;

import com.navigation.entity.Region;
import com.navigation.entity.Ticket;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.RegionService;
import com.navigation.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Resource
    private TicketService ticketService;

    @PostMapping("/save")
    public Result<Void> saveRegion(@RequestBody Ticket ticket){

        return ticketService.saveTicket(ticket);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Ticket ticket){

        return ticketService.update(ticket);
    }

    @DeleteMapping("/batchDelete")
    public Result<Void> batchDelete(@RequestParam List<Integer> ids){

        return ticketService.batchDelete(ids);
    }

    //根据景点id查询门票
    @GetMapping("/queryByScenicId")
    public Ticket queryTicketByScenicId(Integer id){
        return ticketService.queryByScenicId(id);
    }

}