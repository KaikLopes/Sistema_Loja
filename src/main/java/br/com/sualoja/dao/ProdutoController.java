package br.com.sualoja.dao;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}