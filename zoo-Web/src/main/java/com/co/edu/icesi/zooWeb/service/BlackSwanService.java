package com.co.edu.icesi.zooWeb.service;

import com.co.edu.icesi.zooWeb.model.BlackSwan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BlackSwanService {

    public List<BlackSwan> getSwan(@PathVariable String swanName);

    public BlackSwan createSwan(@RequestBody BlackSwan blackSwan);

    public List<BlackSwan> getSwans();

    public BlackSwan updateSwan(String swanName, BlackSwan blackSwan);
}
