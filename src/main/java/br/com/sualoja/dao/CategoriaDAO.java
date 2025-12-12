package br.com.sualoja.dao;

import br.com.sualoja.model.Categoria;
import jakarta.persistence.EntityManager;

public class CategoriaDAO {

    // O EntityManager é a ferramenta principal do JPA para interagir com o banco.
    private EntityManager em;

    // O construtor recebe o EntityManager quando um CategoriaDAO é criado.
    public CategoriaDAO(EntityManager em) {
        this.em = em;
    }

    // Método para salvar uma nova categoria no banco de dados.
    public void cadastrar(Categoria categoria) {
        // O método persist agenda o objeto para ser inserido no banco.
        this.em.persist(categoria);
    }

    // Método para buscar uma categoria pelo seu ID (chave primária).
    public Categoria buscarPorId(Integer id) {
        // O método find busca um objeto pela sua classe e ID.
        return this.em.find(Categoria.class, id);
    }

    // Método para remover uma categoria do banco de dados.
    public void remover(Categoria categoria) {
        // Garante que a entidade está sendo gerenciada antes de remover.
        categoria = this.em.merge(categoria);
        this.em.remove(categoria);
    }
}