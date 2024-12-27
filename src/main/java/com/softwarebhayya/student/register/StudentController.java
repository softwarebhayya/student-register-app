package com.softwarebhayya.student.register;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/students/")
public class StudentController {

	private final StudentRepository studentRepository;
	private static final Logger log = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	public StudentController(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@GetMapping("signup")
	public String showSignUpForm(Student student) {
		return "add-student";
	}

	@GetMapping("list")
	public String showUpdateForm(Model model) {
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}

	@PostMapping("add")
	public String addStudent(@Valid Student student, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-student";
		}
		student.setId(UUID.randomUUID().toString());
		studentRepository.save(student);
		log.info("**** Saved student to DB with name: {}", student.getName());
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		model.addAttribute("student", student);
		return "update-student";
	}

	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") String id, @Valid Student student, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			student.setId(id);
			return "update-student";
		}

		studentRepository.save(student);
		log.info("**** Updated student to DB with name: {}", student.getName());
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable("id") String id, Model model) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		log.info("**** Deleted student to DB with name: {}", student.getName());
		studentRepository.delete(student);
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}
}
