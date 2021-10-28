package br.org.generation.objetivos_aprendizagem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/objetivos_aprendizagem")
public class Objetivos_AprendizagemController {

		@GetMapping
		public String objetivosAprendizagem()
		{
			return "Meu objetivo de aprendizagem dessa semana é o Spring";
		}
}
