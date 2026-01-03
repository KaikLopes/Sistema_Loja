package br.com.sualoja.controller;

import br.com.sualoja.dao.*;
import br.com.sualoja.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaDAO vendaDao;
    @Autowired
    private ClienteDAO clienteDao;
    @Autowired
    private UsuarioDAO usuarioDao;
    @Autowired
    private ProdutoDAO produtoDao;

    @GetMapping
    public List<Venda> listar() {
        return vendaDao.buscarTodos();
    }

    @PostMapping
    public ResponseEntity<?> registrarVenda(@RequestBody VendaInputDTO dados) {
        try {
            Cliente cliente = clienteDao.buscarPorId(dados.clienteId);
            Usuario vendedor = usuarioDao.buscarPorId(dados.vendedorId);
            
            if (cliente == null || vendedor == null) {
                return ResponseEntity.badRequest().body("Cliente ou Vendedor não encontrados.");
            }

            Venda novaVenda = new Venda(cliente, vendedor);

            for (ItemInputDTO itemDTO : dados.itens) {
                Produto produto = produtoDao.buscarPorId(itemDTO.produtoId);
                if (produto != null) {
                    if (produto.getQuantidadeEstoque() < itemDTO.quantidade) {
                        return ResponseEntity.badRequest().body("Estoque insuficiente para: " + produto.getNome());
                    }
                    
                    produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDTO.quantidade);
                    produtoDao.atualizar(produto);

                    VendaItem item = new VendaItem(produto, itemDTO.quantidade);
                    novaVenda.adicionarItem(item);
                }
            }

            vendaDao.cadastrar(novaVenda);
            return ResponseEntity.ok("Venda registrada com sucesso! ID: " + novaVenda.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao processar venda: " + e.getMessage());
        }
    }

    public static class VendaInputDTO {
        public Long clienteId;
        public Long vendedorId;
        public List<ItemInputDTO> itens;
    }

    public static class ItemInputDTO {
        public Integer produtoId;
        public Integer quantidade;
    }
}