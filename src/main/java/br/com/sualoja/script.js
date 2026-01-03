let modalProduto; // Variável para controlar o modal do Bootstrap

document.addEventListener('DOMContentLoaded', () => {
    // Inicializa o Modal do Bootstrap
    modalProduto = new bootstrap.Modal(document.getElementById('modalProduto'));
    carregarProdutos();
});

async function carregarProdutos() {
    const tbody = document.getElementById('tabela-produtos-corpo');
    
    try {
        const resposta = await fetch('/api/produtos');
        const produtos = await resposta.json();

        tbody.innerHTML = ''; // Limpa a tabela

        produtos.forEach(produto => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <th scope="row">${produto.id}</th>
                <td>${produto.nome}</td>
                <td>R$ ${produto.precoVenda.toFixed(2)}</td>
                <td>
                    <span class="badge ${produto.quantidadeEstoque < 5 ? 'bg-danger' : 'bg-success'}">
                        ${produto.quantidadeEstoque} un
                    </span>
                </td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-primary me-1" 
                        onclick="editarProduto(${produto.id}, '${produto.nome}', ${produto.precoVenda}, ${produto.quantidadeEstoque})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deletarProduto(${produto.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (erro) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Erro ao carregar produtos.</td></tr>';
        console.error(erro);
    }
}

async function salvarProduto() {
    const id = document.getElementById('prod-id').value;
    const produto = {
        id: id ? parseInt(id) : null,
        nome: document.getElementById('prod-nome').value,
        precoVenda: parseFloat(document.getElementById('prod-preco').value),
        quantidadeEstoque: parseInt(document.getElementById('prod-estoque').value)
    };

    if(!produto.nome || !produto.precoVenda) {
        alert("Preencha os campos obrigatórios");
        return;
    }

    const metodo = id ? 'PUT' : 'POST';

    try {
        const resposta = await fetch('/api/produtos', {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(produto)
        });

        if (resposta.ok) {
            modalProduto.hide(); // Fecha o modal
            carregarProdutos();
            document.getElementById('form-produto').reset();
        } else {
            alert('Erro ao salvar produto.');
        }
    } catch (erro) {
        console.error(erro);
        alert('Erro de conexão.');
    }
}

async function deletarProduto(id) {
    if (!confirm('Tem certeza que deseja excluir este produto?')) return;

    try {
        await fetch(`/api/produtos/${id}`, { method: 'DELETE' });
        carregarProdutos();
    } catch (erro) {
        console.error(erro);
        alert('Erro ao excluir.');
    }
}

function abrirModalCadastro() {
    document.getElementById('form-produto').reset();
    document.getElementById('prod-id').value = '';
    document.getElementById('modalTitulo').innerText = 'Novo Produto';
    modalProduto.show();
}

function editarProduto(id, nome, preco, estoque) {
    document.getElementById('prod-id').value = id;
    document.getElementById('prod-nome').value = nome;
    document.getElementById('prod-preco').value = preco;
    document.getElementById('prod-estoque').value = estoque;
    
    document.getElementById('modalTitulo').innerText = 'Editar Produto';
    modalProduto.show();
}