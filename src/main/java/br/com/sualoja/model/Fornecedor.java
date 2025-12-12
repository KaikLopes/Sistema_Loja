package br.com.sualoja.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fornecedores") // Nome exato da tabela no banco
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Usamos @Column para ligar o atributo Java 'nomeFantasia'
    // à coluna do banco 'nome_fantasia'
    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    private String cnpj;

    @Column(name = "contato_nome")
    private String contatoNome;

    private String telefone;

    private String email;

    // Construtor padrão obrigatório para o JPA
    public Fornecedor() {
    }

    // Construtor para facilitar a criação
    public Fornecedor(String nomeFantasia, String cnpj) {
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    // --- Getters e Setters para todos os campos ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getContatoNome() {
        return contatoNome;
    }

    public void setContatoNome(String contatoNome) {
        this.contatoNome = contatoNome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}