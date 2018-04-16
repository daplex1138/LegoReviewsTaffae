package edu.dmacc.spring.legoreviewspartner;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LegoController {
	@Autowired
	LegoDao dao;

	private static final String[] ages = { "1-2", "3-5", "6-8", "9-11", "12+" };
	private static final String[] themes = { "Architecture", "City", "Classic", "Disney", "Elves", "Friends", "Juniors",
			"Minecraft", "NexoKnights", "Ninjago", "Star Wars", "Super Heros" };
	private static final String[] reviews = { "1-Bad", "2-OK", "3-Good", "4-Great", "5-Excellent" };

	@RequestMapping(value = "/form")
	public ModelAndView lego() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("legoForm");
		modelAndView.addObject("lego", new Lego());
		modelAndView.addObject("ages", ages);
		modelAndView.addObject("themes", themes);
		return modelAndView;
	}

	@RequestMapping(value = "/reviewForm")
	public ModelAndView review() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("reviewForm");
		modelAndView.addObject("review", new Review());
		List<Lego> allLegos = dao.getLegoIds();
		// List<Lego> allLegos = dao.getAllLegos();
		modelAndView.addObject("allLegos", allLegos);
		// modelAndView.addObject("allLegos", allLegos); //is this duplicate?
		modelAndView.addObject("reviews", reviews);
		return modelAndView;
	}

	@RequestMapping(value = "/result")
	public ModelAndView processLego(Lego lego) {
		ModelAndView modelAndView = new ModelAndView();
		dao.insertLego(lego);
		modelAndView.setViewName("legoResult");
		modelAndView.addObject("l", lego);
		return modelAndView;
	}

	@Bean
	public LegoDao dao() {
		LegoDao bean = new LegoDao();
		return bean;
	}

	@RequestMapping(value = "/reviewResult")
	public ModelAndView processReview(Review review) {
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("I made it");
		dao.insertReview(review);
		modelAndView.setViewName("reviewResult");
		modelAndView.addObject("r", review);
		return modelAndView;
	}

	@RequestMapping(value = "/viewAllLegos")
	public ModelAndView viewAllLegos() {
		ModelAndView modelAndView = new ModelAndView();
		List<Lego> allLegos = dao.getAllLegos();
		modelAndView.setViewName("viewAllLegos");
		modelAndView.addObject("all", allLegos);
		return modelAndView;
	}

	@RequestMapping(value = "/viewAllReviews")
	public ModelAndView viewAllReviews() {
		ModelAndView modelAndView = new ModelAndView();
		List<Review> allReviews = dao.getAllReviews();
		modelAndView.setViewName("viewAllReviews");
		modelAndView.addObject("allR", allReviews);
		return modelAndView;
	}

	@RequestMapping(value = "/deleteLego")
	public ModelAndView deleteLego(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		Integer tempId = Integer.parseInt(request.getParameter("id"));
		Lego legoToDelete = dao.getLegoById(tempId);
		// String id = request.getParameter("id");
		// Lego legoToDelete = dao.getLegoById(7);
		dao.deleteLego(legoToDelete);
		List<Lego> allLegos = dao.getAllLegos();
		modelAndView.setViewName("viewAllLegos");
		System.out.println("We got here--------------");
		modelAndView.addObject("all", allLegos);
		return modelAndView;
	}

	// String act = request.getParameter("doThisToLego");
	/*
	 * System.out.println("delete");//test ModelAndView modelAndView = new
	 * ModelAndView(); //if (act.equals("Delete Lego")){
	 * 
	 * Integer tempId = Integer.parseInt(request.getParameter("id"));
	 * 
	 * Lego legoToDelete = dao.getLegoById(tempId); dao.deleteLego(legoToDelete);
	 * List<Lego> allLegos = dao.getAllLegos();
	 * modelAndView.setViewName("viewAllLegos"); modelAndView.addObject("all",
	 * allLegos);
	 */
	// List<Lego> allLegos = dao.getAllLegos();

	/*
	 * @RequestMapping(value = "/editLego") public ModelAndView processEditLego
	 * (Lego lego) {//not sure need if need httpservetrequest here
	 * System.out.println("Inside edit lego"); ModelAndView modelAndView = new
	 * ModelAndView(); dao.editLego(lego); //Lego editLego = dao.getLegoById(4);
	 * System.out.println(lego.toString());
	 * modelAndView.setViewName("legoResult");//should this be
	 * modelAndView.addObject("ages", ages); modelAndView.addObject("themes",
	 * themes); modelAndView.addObject("e", lego); return modelAndView;
	 */
	// }

	@RequestMapping(value = "/editResult")
	// public ModelAndView processEditResult(HttpServletRequest request,
	// HttpServletResponse response)
	public ModelAndView processEditResult(Lego lego) {
		ModelAndView modelAndView = new ModelAndView();
		dao.editLego(lego);
		List<Lego> allLegos = dao.getAllLegos();
		modelAndView.setViewName("viewAllLegos");
		modelAndView.addObject("all", allLegos);
		return modelAndView;
	}

	@RequestMapping(value = "/editLego") // Or legoUpdate
	public ModelAndView editLego(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = request.getParameter("doThisToLego");
		ModelAndView modelAndView = new ModelAndView();
		if (act.equals("Edit Selected Lego")) {
			String checkId = request.getParameter("id");
			if (checkId == null) {
				modelAndView.setViewName("legoForm");
				return modelAndView;
			}

			Integer tempId = Integer.parseInt(request.getParameter("id"));
			Lego legoToEdit = dao.getLegoById(tempId);
			request.setAttribute("legoToEdit", legoToEdit);
			modelAndView.setViewName("editLego");
			modelAndView.addObject("themes", themes);
			modelAndView.addObject("ageRange", ages);
			modelAndView.addObject("all", legoToEdit);

		} else if (act.equals("Delete Lego")) {
			System.out.println("delete");// test
			String checkId = request.getParameter("id");
			if (checkId == null) {
				modelAndView.setViewName("legoForm");
				return modelAndView;
			}

			Integer tempId = Integer.parseInt(request.getParameter("id"));
			Lego legoToDelete = dao.getLegoById(tempId);
			dao.deleteLego(legoToDelete);
			List<Lego> allLegos = dao.getAllLegos();
			modelAndView.setViewName("viewAllLegos");
			modelAndView.addObject("all", allLegos);
		}
		return modelAndView;
	}

}
