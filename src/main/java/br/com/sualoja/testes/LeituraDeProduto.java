package br.com.sualoja.testes;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class LeituraDeProduto {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();

        // 1. Pegue a ferramenta (o DAO)
        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        // 2. Use a ferramenta para buscar o produto com ID 1
        System.out.println("Buscando o produto de ID 1...");
        Produto produtoEncontrado = produtoDAO.buscarPorId(1);

        // 3. Verifique se o produto foi encontrado antes de usar
        if (produtoEncontrado != null) {
            System.out.println("\n--- PRODUTO ENCONTRADO ---");
            System.out.println("Nome: " + produtoEncontrado.getNome());
            System.out.println("Preço: R$ " + produtoEncontrado.getPrecoVenda());
            System.out.println("Estoque: " + produtoEncontrado.getQuantidadeEstoque());
            // Mostrando como acessar dados de tabelas relacionadas!
            System.out.println("Categoria: " + produtoEncontrado.getCategoria().getNome());
            System.out.println("Fornecedor: " + produtoEncontrado.getFornecedor().getNomeFantasia());
        } else {
            System.out.println("Produto com ID 1 não foi encontrado no banco de dados.");
        }

        // 4. Feche a conexão
        em.close();
        factory.close();
    }
}