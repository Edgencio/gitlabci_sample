/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.services;

import mz.co.technosupport.data.daos.CategoryDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.FeedbackDAO;
import mz.co.technosupport.data.daos.ProblemDAO;
import mz.co.technosupport.data.model.Category;
import mz.co.technosupport.data.model.Feedback;
import mz.co.technosupport.data.model.Problem;
import mz.co.technosupport.info.help.CategoryInfo;
import mz.co.technosupport.info.help.ProblemItemInfo;
import mz.co.technosupport.service.HelpService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author zJohn
 */

@ApplicationScoped
public class HelpServiceImpl implements HelpService {
    
    @Inject
    CategoryDAO categoryDAO;
    @Inject
    ProblemDAO problemDAO;

    @Inject
    ConsumerDAO consumerDAO;

    @Inject
    FeedbackDAO feedbackDAO;

    @Override
    public Collection<CategoryInfo> getCategories() {
        List<CategoryInfo> categoryInfoList = new ArrayList<CategoryInfo>();
        List<Category> categories = new ArrayList<Category>();

        try {
            categories = categoryDAO.getAll();
        } catch (Exception ex) {
           ex.printStackTrace();
           throw new RuntimeException("Falha ao tentar recuperar os Dados na Base de Dados".toUpperCase());
        }
        if(categories == null){
            return categoryInfoList;
        }
        
        for (Category category : categories) {
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setId(category.getId());
            categoryInfo.setName(category.getTitle());
            categoryInfo.setIcon(category.getIconPath());
            categoryInfoList.add(categoryInfo);
        }
        
        return categoryInfoList;
    }

    @Override
    public Collection<ProblemItemInfo> getProblemsOfCategory(long categoryId) {
        List<ProblemItemInfo> problemItemInfoList = new ArrayList<ProblemItemInfo>();
        List<Problem> problems = new ArrayList<Problem>();

        
        try {
            problems = problemDAO.findByCategoryId(categoryId);
        } catch (Exception ex) {
           ex.printStackTrace();
           throw new RuntimeException("Falha ao tentar recuperar os Dados na Base de Dados".toUpperCase());
        }
        if(problems == null){
            return problemItemInfoList;
        }
        
        for (Problem problem : problems) {
            ProblemItemInfo problemInfo = new ProblemItemInfo();
            problemInfo.setId(problem.getId());
            problemInfo.setName(problem.getTitle());
            problemInfo.setDescription(problem.getDescription());
            problemInfo.setSolution(problem.getSolution());
            problemItemInfoList.add(problemInfo);
        }
        
        return problemItemInfoList;
    }

    @Override
    public ProblemItemInfo getProblemById(long problemId) {
        ProblemItemInfo problemReturn = new ProblemItemInfo();
        Problem problem = null;
        try {
            problem = problemDAO.find(problemId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar recuperar Problemas");
        }
        if(problem == null){
            return problemReturn;
        }
        problemReturn.setId(problem.getId());
        problemReturn.setName(problem.getTitle());
        problemReturn.setDescription(problem.getDescription());
        problemReturn.setSolution(problem.getSolution());
        
        return problemReturn;
    }

    @Override
    public void saveClientFeedback(long l, long l1, double v, String s) {

        Feedback feedback = new Feedback();
        try {
            feedback.setConsumer(consumerDAO.find(l));
            feedback.setProblem(problemDAO.find(l1));
            feedback.setRate(v);
            feedback.setMessage(s);
            feedbackDAO.create(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
