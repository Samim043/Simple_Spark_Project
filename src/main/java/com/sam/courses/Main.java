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

        before((req, res)->{
            if(req.cookie("username") !=null){
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/ideas", (req, res)->{
            //TODO:csd -- Send message about redirect somehow....
            if(req.attribute("username") ==null){
                res.redirect("/");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.attribute("username"));
            return new ModelAndView(model,"index.hbs");
            },new HandlebarsTemplateEngine());

        post("/", (req, res)->{
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");
            res.cookie("username", username);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/ideas", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            model.put("ideas",dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) ->{
            String title = req.queryParams("title");
            CourseIdea courseIdea = new CourseIdea(title,req.attribute("username"));
            dao.add(courseIdea);
            res.redirect("/ideas");
            return null;
        }) ;

        get("/ideas/:slug", (req, res)->{
           Map<String, Object> model = new HashMap<>();
           model.put("idea", dao.findBySlug(req.params("slug")));
           return new ModelAndView(model, "idea.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas/:slug/vote", (req, res)->{
            CourseIdea idea = dao.findBySlug(req.params("slug"));
            idea.addVoter(req.attribute("username"));
            res.redirect("/ideas");
            return null;
        });

    }
}
