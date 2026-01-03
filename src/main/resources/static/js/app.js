let modalProdutoInstancia;
let modalVendaInstancia;
let listaProdutosCache = [];
let contadorItens = 0;

document.addEventListener('DOMContentLoaded', () => {
    modalProdutoInstancia = new bootstrap.Modal(document.getElementById('modalProduto'));
    modalVendaInstancia = new bootstrap.Modal(document.getElementById('modalVenda'));
    
    document.getElementById('menu-toggle').addEventListener('click', () => {
        document.getElementById('wrapper').classList.toggle('toggled');
    });

    carregarView('dashboard');
});

async function carregarView(view) {
    const container = document.getElementById('conteudo-principal');
    const titulo = document.getElementById('titulo-pagina');
    
    document.querySelectorAll('.list-group-item').forEach(el => el.classList.remove('active-link', 'text-warning'));
    if(event && event.target) event.target.closest('a')?.classList.add('active-link', 'text-warning');

    if (view === 'dashboard') {
        titulo.innerText = 'Dashboard Financeiro';
        await renderizarDashboard(container);
    } else if (view === 'produtos') {
        titulo.innerText = 'Gerenciamento de Produtos';
        await renderizarListaProdutos(container);
    } else if (view === 'vendas') {
        titulo.innerText = 'Gestão de Vendas';
        await renderizarHistoricoVendas(container);
    }
}

async function renderizarDashboard(container) {
    container.innerHTML = '<div class="text-center mt-5"><div class="spinner-border text-primary"></div><br>Calculando lucros...</div>';

    try {
        // 1. Busca Produtos e Vendas ao mesmo tempo
        const [resProdutos, resVendas] = await Promise.all([
            fetch('/api/produtos'),
            fetch('/api/vendas')
        ]);

        const produtos = await resProdutos.json();
        const vendas = await resVendas.json();

        // --- CÁLCULOS ---

        // 1. Faturamento Total (Soma o valor de todas as vendas)
        const faturamentoTotal = vendas.reduce((acc, venda) => acc + venda.valorTotal, 0);

        // 2. Valor em Estoque (Preço * Quantidade de cada produto parado)
        const valorEstoque = produtos.reduce((acc, p) => acc + (p.precoVenda * p.quantidadeEstoque), 0);

        // 3. Produtos Mais Vendidos (Lógica de Ranking)
        const mapaVendas = {}; // Objeto para contar: { "Notebook": 5, "Mouse": 10 }

        vendas.forEach(venda => {
            venda.itens.forEach(item => {
                const nomeProd = item.produto.nome;
                // Se já existe soma a quantidade, se não começa com 0
                mapaVendas[nomeProd] = (mapaVendas[nomeProd] || 0) + item.quantidade;
            });
        });

        // Transforma o objeto em lista, ordena do maior para o menor e pega o Top 5
        const topProdutos = Object.entries(mapaVendas)
            .map(([nome, qtd]) => ({ nome, qtd }))
            .sort((a, b) => b.qtd - a.qtd) // Ordena decrescente
            .slice(0, 5); // Pega só os 5 primeiros

        // --- HTML (Visual) ---
        
        container.innerHTML = `
            <div class="row g-3 mb-4">
                <div class="col-md-4">
                    <div class="card p-3 shadow-sm border-start border-4 border-success">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <p class="mb-1 text-muted text-uppercase small fw-bold">Faturamento Total</p>
                                <h3 class="fw-bold text-success">R$ ${faturamentoTotal.toFixed(2)}</h3>
                            </div>
                            <i class="fas fa-sack-dollar fa-2x text-success opacity-25"></i>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card p-3 shadow-sm border-start border-4 border-primary">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <p class="mb-1 text-muted text-uppercase small fw-bold">Valor em Estoque</p>
                                <h3 class="fw-bold text-primary">R$ ${valorEstoque.toFixed(2)}</h3>
                            </div>
                            <i class="fas fa-boxes fa-2x text-primary opacity-25"></i>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card p-3 shadow-sm border-start border-4 border-info">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <p class="mb-1 text-muted text-uppercase small fw-bold">Vendas Realizadas</p>
                                <h3 class="fw-bold text-info">${vendas.length}</h3>
                            </div>
                            <i class="fas fa-shopping-cart fa-2x text-info opacity-25"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white py-3 border-0">
                            <h5 class="mb-0 fw-bold text-secondary"><i class="fas fa-trophy me-2 text-warning"></i>Top 5 Produtos Mais Vendidos</h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0 align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Produto</th>
                                            <th class="text-center">Quantidade Vendida</th>
                                            <th style="width: 30%;">Popularidade</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        ${topProdutos.length > 0 ? topProdutos.map(p => {
                                            // Lógica visual da barra de progresso
                                            const maiorQtd = topProdutos[0].qtd;
                                            const percentual = (p.qtd / maiorQtd) * 100;
                                            
                                            return `
                                            <tr>
                                                <td class="fw-bold text-dark">${p.nome}</td>
                                                <td class="text-center fs-5 fw-bold">${p.qtd}</td>
                                                <td>
                                                    <div class="progress" style="height: 8px;">
                                                        <div class="progress-bar bg-warning" role="progressbar" style="width: ${percentual}%"></div>
                                                    </div>
                                                </td>
                                            </tr>
                                            `
                                        }).join('') : '<tr><td colspan="3" class="text-center py-4 text-muted">Nenhuma venda realizada ainda.</td></tr>'}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

    } catch (e) {
        console.error(e);
        container.innerHTML = `<div class="alert alert-danger">Erro ao carregar dados da dashboard: ${e.message}</div>`;
    }
}

async function renderizarListaProdutos(container) {
    const produtos = await buscarDadosAPI();
    listaProdutosCache = produtos;

    let htmlTabela = `
        <div class="row mb-3">
            <div class="col-md-8">
                <input type="text" class="form-control" id="buscaProduto" placeholder="Buscar por nome..." onkeyup="filtrarTabelaLocal()">
            </div>
            <div class="col-md-4 text-end">
                <button class="btn btn-primary" onclick="abrirModal()">
                    <i class="fas fa-plus"></i> Novo
                </button>
            </div>
        </div>
        <div class="card shadow-sm">
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>Nome</th>
                            <th>Preço</th>
                            <th>Estoque</th>
                            <th class="text-end">Ações</th>
                        </tr>
                    </thead>
                    <tbody id="tbody-produtos">
                        ${gerarLinhasTabela(produtos)}
                    </tbody>
                </table>
            </div>
        </div>
    `;
    container.innerHTML = htmlTabela;
}

function gerarLinhasTabela(produtos) {
    if (produtos.length === 0) return '<tr><td colspan="4" class="text-center p-3">Nenhum produto encontrado.</td></tr>';
    
    return produtos.map(p => `
        <tr>
            <td class="align-middle fw-bold">${p.nome}</td>
            <td class="align-middle">R$ ${p.precoVenda.toFixed(2)}</td>
            <td class="align-middle">
                <span class="badge ${p.quantidadeEstoque < 5 ? 'bg-danger' : 'bg-success'}">
                    ${p.quantidadeEstoque} un
                </span>
            </td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-primary me-1" onclick="editar(${p.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="deletar(${p.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

async function renderizarHistoricoVendas(container) {
    let vendas = [];
    try {
        const res = await fetch('/api/vendas');
        vendas = await res.json();
    } catch (e) { console.error("Erro ao buscar vendas", e); }

    let html = `
        <div class="d-flex justify-content-between mb-3">
            <h3>Histórico de Transações</h3>
            <button class="btn btn-success" onclick="abrirModalVenda()">
                <i class="fas fa-cart-plus"></i> Nova Venda
            </button>
        </div>
        <div class="card shadow-sm">
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Data</th>
                            <th>Cliente</th>
                            <th>Total</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${vendas.map(v => `
                            <tr>
                                <td>#${v.id}</td>
                                <td>${v.dataVenda ? new Date(v.dataVenda).toLocaleDateString() : '-'}</td>
                                <td>${v.cliente ? v.cliente.nome : 'Cliente Removido'}</td>
                                <td class="fw-bold text-success">R$ ${v.valorTotal.toFixed(2)}</td>
                                <td><span class="badge bg-success">Concluída</span></td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        </div>
    `;
    container.innerHTML = html;
}

// --- Funções Auxiliares e API ---

async function buscarDadosAPI() {
    try {
        const res = await fetch('/api/produtos');
        return await res.json();
    } catch (e) {
        return [];
    }
}

async function salvarProduto() {
    const id = document.getElementById('prod-id').value;
    const produto = {
        nome: document.getElementById('prod-nome').value,
        precoVenda: parseFloat(document.getElementById('prod-preco').value),
        quantidadeEstoque: parseInt(document.getElementById('prod-estoque').value)
    };

    if (id) produto.id = id;

    const method = id ? 'PUT' : 'POST';
    
    await fetch('/api/produtos', {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(produto)
    });

    modalProdutoInstancia.hide();
    carregarView('produtos');
}

async function deletar(id) {
    if(confirm('Remover este item?')) {
        await fetch(`/api/produtos/${id}`, { method: 'DELETE' });
        carregarView('produtos');
    }
}

function filtrarTabelaLocal() {
    const termo = document.getElementById('buscaProduto').value.toLowerCase();
    const filtrados = listaProdutosCache.filter(p => p.nome.toLowerCase().includes(termo));
    document.getElementById('tbody-produtos').innerHTML = gerarLinhasTabela(filtrados);
}

function abrirModal() {
    document.getElementById('form-produto').reset();
    document.getElementById('prod-id').value = '';
    document.getElementById('modalTitulo').innerText = 'Novo Produto';
    modalProdutoInstancia.show();
}

function editar(id) {
    const p = listaProdutosCache.find(x => x.id === id);
    if(p) {
        document.getElementById('prod-id').value = p.id;
        document.getElementById('prod-nome').value = p.nome;
        document.getElementById('prod-preco').value = p.precoVenda;
        document.getElementById('prod-estoque').value = p.quantidadeEstoque;
        document.getElementById('modalTitulo').innerText = 'Editar Produto';
        modalProdutoInstancia.show();
    }
}

// --- Funções de Venda ---

async function abrirModalVenda() {
    document.getElementById('lista-itens-venda').innerHTML = '';
    document.getElementById('venda-total-estimado').innerText = '0.00';
    contadorItens = 0;
    
    // Atualiza cache para garantir opções no select
    listaProdutosCache = await buscarDadosAPI();
    
    adicionarLinhaItem();
    modalVendaInstancia.show();
}

function adicionarLinhaItem() {
    contadorItens++;
    const div = document.createElement('div');
    div.className = 'row g-2 mb-2 align-items-end item-venda-row';
    div.id = `linha-item-${contadorItens}`;
    
    const opcoesProdutos = listaProdutosCache.map(p => 
        `<option value="${p.id}" data-preco="${p.precoVenda}">${p.nome} (Estoque: ${p.quantidadeEstoque})</option>`
    ).join('');

    div.innerHTML = `
        <div class="col-7">
            <select class="form-select produto-select" onchange="calcularTotalVenda()">
                <option value="" selected disabled>Selecione um produto...</option>
                ${opcoesProdutos}
            </select>
        </div>
        <div class="col-3">
            <input type="number" class="form-control quantidade-input" value="1" min="1" onchange="calcularTotalVenda()">
        </div>
        <div class="col-2">
            <button type="button" class="btn btn-outline-danger w-100" onclick="removerLinha(${contadorItens})">
                <i class="fas fa-times"></i>
            </button>
        </div>
    `;
    
    document.getElementById('lista-itens-venda').appendChild(div);
}

function removerLinha(id) {
    const linha = document.getElementById(`linha-item-${id}`);
    if(linha) linha.remove();
    calcularTotalVenda();
}

function calcularTotalVenda() {
    let total = 0;
    document.querySelectorAll('.item-venda-row').forEach(row => {
        const select = row.querySelector('.produto-select');
        const qtd = row.querySelector('.quantidade-input').value;
        
        if (select.selectedIndex > 0) {
            const preco = parseFloat(select.options[select.selectedIndex].dataset.preco);
            total += preco * qtd;
        }
    });
    document.getElementById('venda-total-estimado').innerText = total.toFixed(2);
}

async function finalizarVenda() {
    const clienteId = document.getElementById('venda-cliente').value;
    const vendedorId = document.getElementById('venda-vendedor').value;
    
    const itens = [];
    document.querySelectorAll('.item-venda-row').forEach(row => {
        const select = row.querySelector('.produto-select');
        const qtd = row.querySelector('.quantidade-input').value;
        
        if (select.value) {
            itens.push({
                produtoId: parseInt(select.value),
                quantidade: parseInt(qtd)
            });
        }
    });

    if (itens.length === 0) {
        alert("Adicione pelo menos um produto.");
        return;
    }

    const payload = {
        clienteId: parseInt(clienteId),
        vendedorId: parseInt(vendedorId),
        itens: itens
    };

    try {
        const res = await fetch('/api/vendas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            alert("Venda realizada com sucesso!");
            modalVendaInstancia.hide();
            carregarView('vendas');
        } else {
            const erro = await res.text();
            alert("Erro: " + erro);
        }
    } catch (e) {
        alert("Erro de conexão.");
    }
}