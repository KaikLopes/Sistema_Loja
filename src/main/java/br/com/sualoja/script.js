document.addEventListener('DOMContentLoaded', () => {
    carregarProdutos();
});

async function carregarProdutos() {
    const container = document.getElementById('lista-produtos');
    
    try {
        const resposta = await fetch('/api/produtos');
        const produtos = await resposta.json();

        container.innerHTML = ''; // Limpa o "Carregando..."

        produtos.forEach(produto => {
            const card = document.createElement('div');
            card.classList.add('card-produto');

            card.innerHTML = `
                <h3>${produto.nome}</h3>
                <p>${produto.descricao || 'Sem descrição'}</p>
                <p class="preco">R$ ${produto.precoVenda.toFixed(2)}</p>
                <p>Estoque: ${produto.quantidadeEstoque}</p>
            `;
            container.appendChild(card);
        });
    } catch (erro) {
        container.innerHTML = '<p>Erro ao carregar produtos.</p>';
        console.error(erro);
    }
}