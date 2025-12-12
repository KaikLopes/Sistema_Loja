package br.com.sualoja.dao;

import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProdutoDAO {

    private EntityManager em;

    public ProdutoDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Produto produto) {
        this.em.persist(produto);
    }

    public Produto buscarPorId(Integer id) {
        return this.em.find(Produto.class, id);
    }

    public void atualizar(Produto produto) {
        this.em.merge(produto);
    }

    public void remover(Produto produto) {
        produto = this.em.merge(produto);
        this.em.remove(produto);
    }

    public List<Produto> buscarTodos() {
    // JPQL (Java Persistence Query Language) - é como SQL, mas para objetos Java
    String jpql = "SELECT p FROM Produto p"; // "Selecione todos os objetos 'p' do tipo 'Produto'"
    return em.createQuery(jpql, Produto.class).getResultList();
}
}