package br.com.sualoja.testes; // <-- A prateleira onde este arquivo mora

// ▼▼▼ As instruções de onde encontrar os ingredientes e ferramentas ▼▼▼
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AtualizarEstoque {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();

        // 1. Pegue a sua melhor faca de produtos (a ferramenta DAO)
        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        em.getTransaction().begin();

        // 2. Use a faca para buscar o ingrediente que você quer alterar (o produto)
        // A furadeira que cadastramos tem o ID 1
        Produto furadeira = produtoDAO.buscarPorId(1); 
        
        // 3. Verifique se o produto existe antes de tentar alterar
        if (furadeira != null) {
            System.out.println("Estoque atual: " + furadeira.getQuantidadeEstoque());

            // 4. Altere o ingrediente
            furadeira.setQuantidadeEstoque(15); // O estoque agora é 15
            System.out.println("Novo estoque: " + furadeira.getQuantidadeEstoque());

            // 5. Use a faca novamente para salvar a alteração (fazer o UPDATE)
            produtoDAO.atualizar(furadeira);
            System.out.println(">>> Estoque atualizado com sucesso!");
        } else {
            System.out.println("Produto com ID 1 não encontrado.");
        }

        em.getTransaction().commit();
        
        em.close();
        factory.close();
    }
}