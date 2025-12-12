package br.com.sualoja.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "VendasItens")
public class VendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // No Java é mais fácil ter um ID próprio, mesmo que no banco seja composto

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;

    @Column(name = "preco_unitario_venda")
    private BigDecimal precoUnitario;
    
    @Column(name = "custo_unitario_compra")
    private BigDecimal custoUnitario; // Para calcular lucro depois

    public VendaItem() {}

    public VendaItem(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco(); 
        this.custoUnitario = new BigDecimal("10.00"); 
    }

    // Getters e Setters...
    public void setVenda(Venda venda) { this.venda = venda; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public Integer getQuantidade() { return quantidade; }
}