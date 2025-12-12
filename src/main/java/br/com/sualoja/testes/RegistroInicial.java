package br.com.sualoja.testes;

import java.math.BigDecimal;
import br.com.sualoja.dao.CategoriaDAO;
import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Categoria;
import br.com.sualoja.model.Fornecedor;
import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Este é o nosso "script" para a primeira carga de dados no sistema.
public class RegistroInicial {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();

        // Criamos todos os "tratadores" que vamos usar
        CategoriaDAO categoriaDAO = new CategoriaDAO(em);
        FornecedorDAO fornecedorDAO = new FornecedorDAO(em);
        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        // --- Início da Transação: tudo abaixo acontece de uma vez ---
        em.getTransaction().begin();

        // 1. DADOS DA CATEGORIA
        Categoria novaCategoria = new Categoria("Ferramentas Elétricas");
        categoriaDAO.cadastrar(novaCategoria);
        System.out.println(">>> CATEGORIA CADASTRADA: " + novaCategoria.getNome() + " | ID: " + novaCategoria.getId());

        // 2. DADOS DO FORNECEDOR
        Fornecedor novoFornecedor = new Fornecedor("Sorriso Ferramentas", "12.345.678/0001-99");
        novoFornecedor.setContatoNome("Ana Silva");
        fornecedorDAO.cadastrar(novoFornecedor);
        System.out.println(">>> FORNECEDOR CADASTRADO: " + novoFornecedor.getNomeFantasia() + " | ID: " + novoFornecedor.getId());

        // 3. DADOS DO PRODUTO
        Produto novoProduto = new Produto();
        novoProduto.setNome("Furadeira Parafusadeira Impacto Kit 2 Baterias 21v Maleta");
        novoProduto.setDescricao("Furadeira e Parafusadeira de Impacto com kit e maleta.");
        novoProduto.setPrecoVenda(new BigDecimal("145.41")); // Lembre-se do ponto!
        novoProduto.setQuantidadeEstoque(10);
        novoProduto.setUnidadeMedida("UN");

        // Ligando o produto à categoria e ao fornecedor que acabamos de criar
        novoProduto.setCategoria(novaCategoria);
        novoProduto.setFornecedor(novoFornecedor);

        produtoDAO.cadastrar(novoProduto);
        System.out.println(">>> PRODUTO CADASTRADO: " + novoProduto.getNome() + " | ID: " + novoProduto.getId());

        // --- Fim da Transação: Salva tudo no banco de dados ---
        em.getTransaction().commit();
        System.out.println("\n>>> SUCESSO! Transação confirmada e todos os dados foram salvos!");

        em.close();
        factory.close();
    }
}