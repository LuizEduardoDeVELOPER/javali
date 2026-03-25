document.addEventListener('DOMContentLoaded', function() {
    // SEÇÃO 1: DADOS DO CARROSSEL HERO (Adicionando a Imagem Local)
   const slides = [
    {
        img: '/img/imgtenda1.jpeg', // Certifique-se da barra inicial e da extensão .jpeg
        titulo: 'VENDAS DO MÊS',
        desc: 'Sua estrutura de eventos com a autoridade de quem fabrica na QNH 3.'
    },
        {
            img: 'img', // Banner de Qualidade
            titulo: 'ENTREGUES HOJE',
            desc: 'Estruturas reforçadas em Lona PVC para aguentar o sol de Brasília.'
        },
        {
            img: 'https://i.postimg.cc/85zR6YpZ/tenda-piramidal.jpg', // Detalhe de Lona
            titulo: 'QUALIDADE QNH 3',
            desc: 'Fabricação própria e durabilidade garantida para seu evento no DF.'
        }
    ];

    let current = 0;
    const slider = document.getElementById('sliderEntregas');
    const titulo = document.getElementById('tituloBanner');
    const desc = document.getElementById('descricaoBanner');

    function mudarSlide() {
        if(slider) {
            // Aplica a imagem e o gradiente para manter a legibilidade
            slider.style.backgroundImage = `linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.9)), url('${slides[current].img}')`;
            if (titulo) titulo.innerText = slides[current].titulo;
            if (desc) desc.innerText = slides[current].desc;
            
            current = (current + 1) % slides.length;
        }
    }

    // Intervalo de 4 segundos para o cliente conseguir ler
    setInterval(mudarSlide, 4000);
    mudarSlide(); // Inicia imediatamente
});