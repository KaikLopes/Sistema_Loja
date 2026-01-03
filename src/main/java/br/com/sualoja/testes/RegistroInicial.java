package br.com.sualoja.testes;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.UsuarioDAO;
import br.com.sualoja.dao.ClienteDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.model.Produto;
import br.com.sualoja.model.Usuario;
import br.com.sualoja.model.Cliente;
import br.com.sualoja.model.Venda;
import br.com.sualoja.model.VendaItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class RegistroInicial {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();
        
        ProdutoDAO produtoDao = new ProdutoDAO(em);
        UsuarioDAO usuarioDao = new UsuarioDAO(em);
        ClienteDAO clienteDao = new ClienteDAO(em);
        VendaDAO vendaDao = new VendaDAO(em);

        em.getTransaction().begin();

        Usuario vendedor = new Usuario("Kaik Lopes", "111.222.333-44", "kaik.dev", "senha123");
        usuarioDao.cadastrar(vendedor);

        Cliente cliente = new Cliente("Lucas Pereira", "999.888.777-66", "8399999-0000", "Rua do IFPB, Cajazeiras");
        clienteDao.cadastrar(cliente);

        Produto produto = produtoDao.buscarPorId(1);

        Venda venda = new Venda(cliente, vendedor);
        VendaItem item = new VendaItem(produto, 2);
        venda.adicionarItem(item);

        vendaDao.cadastrar(venda);

        em.getTransaction().commit();
        
        System.out.println("Venda realizada com sucesso! ID: " + venda.getId());
        em.close();
    }
}