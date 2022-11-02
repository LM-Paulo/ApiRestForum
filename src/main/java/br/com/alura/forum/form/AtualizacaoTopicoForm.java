package br.com.alura.forum.form;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoReposity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class AtualizacaoTopicoForm {

    @NotBlank
    @Length(min = 5)
    private String titulo;
    @NotBlank @Length(min = 10)
    private String mensagem;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Topico atualizar(Long id, TopicoReposity topicoReposity) {
        Topico topico = topicoReposity.getOne(id);
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);
        return topico;
    }
}
