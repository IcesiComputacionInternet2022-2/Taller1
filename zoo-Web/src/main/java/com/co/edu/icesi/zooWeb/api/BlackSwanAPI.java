package com.co.edu.icesi.zooWeb.api;

import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RequestMapping("/black-swan")
    public interface BlackSwanAPI{

        @GetMapping("/{swanName}")
        public List<BlackSwanDTO> getSwan(@PathVariable String swanName);

        @PostMapping
        BlackSwanDTO createSwan(@RequestBody BlackSwanDTO blackSwanDTO);

        @GetMapping
        public List<BlackSwanDTO> getSwans();

        @PostMapping("/{swanName}")
        BlackSwanDTO updateSwan(@PathVariable String swanName, @RequestBody BlackSwanDTO blackSwanDTO);

    }

