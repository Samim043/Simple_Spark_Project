package com.sam.courses;

import com.sam.courses.model.CourseIdea;
import com.sam.courses.model.CourseIdeaDAO;
import com.sam.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.Spark.before;
import static spark.Spark.halt;

public class Main {
    public static void main(String[] args) {

        staticFileLocation("/public");

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        before("/ideas", (req, res)->{
            //TODO:csd -- Send message about redirect somehow....
            if(req.cookie("username") ==null){
                res.redirect("/");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));
            return new ModelAndView(model,"index.hbs");
            },new HandlebarsTemplateEngine());

        post("/sign-in", (req, res)->{
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");
            res.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model,"sign-in.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            model.put("ideas",dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) ->{
            String title = req.queryParams("title");
            //TODO:csd -- This username is tied to the cookie implementation
            CourseIdea courseIdea = new CourseIdea(title,req.cookie("username"));
            dao.add(courseIdea);
            res.redirect("/ideas");
            return null;
        }) ;
    }
}