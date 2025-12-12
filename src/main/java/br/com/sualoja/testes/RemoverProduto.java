package br.com.sualoja.testes; // <-- A prateleira onde este arquivo mora

// ▼▼▼ As instruções de onde encontrar os ingredientes e ferramentas ▼▼▼
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class RemoverProduto {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();
        
        // 1. Pegue a faca de produtos
        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        em.getTransaction().begin();

        // 2. Use a faca para buscar o produto que será removido
        Produto produtoParaRemover = produtoDAO.buscarPorId(1);
        
        // 3. Verifique se o produto existe antes de tentar remover
        if (produtoParaRemover != null) {
            System.out.println("Removendo o produto: " + produtoParaRemover.getNome());

            // 4. Use a ferramenta de remoção da faca
            produtoDAO.remover(produtoParaRemover);
            System.out.println(">>> Produto removido com sucesso!");
        } else {
            System.out.println("Produto com ID 1 não encontrado para remoção.");
        }
        
        em.getTransaction().commit();

        em.close();
        factory.close();
    }
}