package br.org.generation.lojagames.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.org.generation.lojagames.model.Usuario;
import br.org.generation.lojagames.model.UsuarioLogin;
import br.org.generation.lojagames.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	public Optional <Usuario> cadastrarUsuario (Usuario usuario)
	{
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O usuário já existe", null); 
		
		if(calcularIdade(usuario.getDataNascimento()) < 18)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O usuário é menor de idade",null);
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of(usuarioRepository.save(usuario));
	}
	
	public Optional <Usuario> atualizarUsuario(Usuario usuario)
	{
		if(usuarioRepository.findById(usuario.getId()).isPresent()) 
		{
			Optional<Usuario> buscarUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
			if(buscarUsuario.isPresent())
			{
				if (buscarUsuario.get().getId() != usuario.getId())
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O usuário já existe",null);
			}
			
			if(calcularIdade(usuario.getDataNascimento()) < 18)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O usuário é menor de idade", null);
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			return Optional.of(usuarioRepository.save(usuario));
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado!",null);	
	}
	
	public Optional <UsuarioLogin> autenticarUsuario(Optional <UsuarioLogin> usuarioLogin)
	{
		Optional <Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent())
		{
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha()))
			{
				String token = gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());
				
				usuarioLogin.get().setId(usuario.get().getId());				
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				usuarioLogin.get().setToken(token);
				
				return usuarioLogin;
			}
		}
		
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuário ou senha inválidos!", null);	
	}
	
	private String criptografarSenha(String senha)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	}
	
	private Boolean compararSenhas (String senhaDigitada, String senhaBanco)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	private String gerarBasicToken (String usuario, String senha)
	{
		String tokenBase = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(tokenBase.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
	}
	
	private int calcularIdade (LocalDate dataNascimento)
	{
		return Period.between(dataNascimento, LocalDate.now()).getYears();
	}
}
