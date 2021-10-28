package br.org.generation.habilidades_mentalidades.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/habilidades_mentalidades")
public class HabilidadesMentalidadesController {

		@GetMapping
		public String habMent ()
		{
			return "Eu utilizei a habilidade de atenção aos detalhes e a mentalidade de persistência para realizar essa tarefa!";
		}
}
