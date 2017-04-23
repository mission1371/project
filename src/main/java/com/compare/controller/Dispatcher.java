package com.compare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by umut.taherzadeh on 2017-04-22.
 */
@Controller
public class Dispatcher {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        return "index";
    }

//    @RequestMapping(value = "/views/*", method = RequestMethod.GET)
//    public String others(ModelMap model) {
//        return "/views/java.view";
//    }

    @RequestMapping(value = "/public/views/*", method = RequestMethod.GET)
    public String others2(ModelMap model) {
        return "/views/java.view";
    }

//    @RequestMapping(value = "public/views/*", method = RequestMethod.GET)
//    public String others5(ModelMap model) {
//        return "/views/java.view";
//    }

}
