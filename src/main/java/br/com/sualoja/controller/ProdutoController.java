package br.com.sualoja.controller;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoDAO produtoDAO;

    @GetMapping
    public List<Produto> listar() {
        return produtoDAO.buscarTodos();
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody Produto produto) {
        produtoDAO.cadastrar(produto);
        return ResponseEntity.ok("Produto cadastrado com sucesso!");
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody Produto produto) {
        produtoDAO.atualizar(produto);
        return ResponseEntity.ok("Produto atualizado com sucesso!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Integer id) {
        Produto produto = produtoDAO.buscarPorId(id);
        if (produto != null) {
            produtoDAO.remover(produto);
            return ResponseEntity.ok("Produto removido!");
        }
        return ResponseEntity.notFound().build();
    }
}