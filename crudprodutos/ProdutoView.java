package crudprodutos; // declara que a classe pertence ao crud produtos, e organiza arquivos//

// importancoes necessarias //
import javax.swing.*; //importa todos os componentes gráficos(Jframe,JTable,JButtom)//
import javax.swing.table.DefaultTableModel; //importa Modelo da tabela(adicionar linhas, remover e atualizar)//
import java.awt.*; //importa layout e eventos

// classe produtosview herda atributos, metodos do JFrame(Uma janela gráfica) //
public class ProdutoView extends JFrame {
   
    /*criando um controlador, para manipular os produtos(adicionar, remover e atualizar) */
    private ProdutoController controller = new ProdutoController();

    /* compos de respostas(nome do produto, preço do produto e quantidade do produto) */
    private JTextField nomeField = new JTextField(10);
    private JTextField precoField = new JTextField(10);
    private JTextField quantidadeField = new JTextField(10);

    /* criando estrutura(A tabela visual) */
    private JTable tabela;

    /* representa a tabela internamente */
    private DefaultTableModel modelo;
    
    //Tudo aqui dentro, faz parte da configuração da janela//
    public ProdutoView() {

        /* organizando informações */
        JPanel form = new JPanel(); //painel que vai segurar os campos e botões
        setTitle("Gerenciador de Produtos"); /* nome da janela */
        setSize(600, 400); /* resolução */
        setLocationRelativeTo(null); /* centralizar */
        setDefaultCloseOperation(EXIT_ON_CLOSE); /* encerrar programa */
        setLayout(new BorderLayout());  /* permite utilizar áreas norte, sul, centro e etc. */
        
        /* adicionando informações na janela(textos e campos de respostas) */
        form.add(new JLabel("Nome:"));
        form.add(nomeField);
        form.add(new JLabel("Preço:"));
        form.add(precoField);
        form.add(new JLabel("Qtd:"));
        form.add(quantidadeField);

        /* criando os botões das ações(adicionar, atualizar e remover) */
        JButton addBtn = new JButton("Adicionar");
        JButton updateBtn = new JButton("Atualizar");
        JButton removeBtn = new JButton("Remover");

        /* adicionando os botões na interface */
        form.add(addBtn);
        form.add(updateBtn);
        form.add(removeBtn);
        add(form, BorderLayout.NORTH); //adiciona esse painel no canto superior da interface

        /* organizando os elementos  */
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Qtd"}, 0); //cria modelo com 4 colunas(id,nome,preço e quantidade)
        tabela = new JTable(modelo);//cria tabela gráfica
        add(new JScrollPane(tabela), BorderLayout.CENTER); //permite rolagem e adciona ao centro da janela

        /* definincdo ação do botão de adicionar */
        addBtn.addActionListener(e -> {
            try {
                /* coletando informações e contertendo elas */
                String nome = nomeField.getText(); 
                double preco = Double.parseDouble(precoField.getText());
                int qtd = Integer.parseInt(quantidadeField.getText());

                /* adicionando respostas adquiridas e criando novo produto */
                controller.adicionar(new Produto(0, nome, qtd, preco));

                limparCampos(); /* limpar tela  */
                listar(); /* listar informações */ 
                JOptionPane.showMessageDialog(this, "Produto adicionado!"); // mensagem de produto adiciondo 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro nos dados!"); // informações invalidas(caso dê algum erro)
            }
        });
        // definindo ação do botão de atualizar 
        updateBtn.addActionListener(e -> {

            // pegando linha selecionada 
            int linha = tabela.getSelectedRow(); 
            // se a pessoa selecionar uma linha
            if (linha >= 0) {
                // preenchendo infromações anteriores
                int id = (int) modelo.getValueAt(linha, 0); //pega o id

                //pega as informações digitadas
                String nome = nomeField.getText();
                double preco = Double.parseDouble(precoField.getText());
                int qtd = Integer.parseInt(quantidadeField.getText());
                
                // adicionando informações atualizadas pelo controller 
                if (controller.atualizar(id, new Produto(id, nome, qtd, preco))) {
                    listar();
                    limparCampos();
                    JOptionPane.showMessageDialog(this, "Produto atualizado!");
                }
                //se não selecionar nenhuma linha
            } else JOptionPane.showMessageDialog(this, "Selecione um produto!");
        });

        // ação do botão de remover 
        removeBtn.addActionListener(e -> {
            int linha = tabela.getSelectedRow(); //pega a linha selecionada
            //se pegar linha selecionada ele executa a condição
            if (linha >= 0) {
                int id = (int) modelo.getValueAt(linha, 0); //retorna id da coluna
                controller.remover(id); //remove todo o objeto com base na coluna
                listar(); //lista os produtos novamente
                JOptionPane.showMessageDialog(this, "Produto removido!"); //mensagem após remover
            //caso não selecione nenhuma linha
            } else JOptionPane.showMessageDialog(this, "Selecione um produto!");
        });

        //Ação ao clicar numa linha
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                //pega a linha
                int linha = tabela.getSelectedRow();
                //preenche automaticamente as informações
                if (linha >= 0) {
                nomeField.setText(modelo.getValueAt(linha, 1).toString());
                precoField.setText(modelo.getValueAt(linha, 2).toString());
                quantidadeField.setText(modelo.getValueAt(linha, 3).toString());
        }
    }
});

    }
    // percorrendo lista e exibindo produto 
    private void listar() {
        modelo.setRowCount(0);
        // looping que percorre lista 
        for (Produto p : controller.listar()) {
            // retornando informações 
            modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }
    
    // limpar a tela 
    private void limparCampos() {
        nomeField.setText("");
        precoField.setText("");
        quantidadeField.setText("");
    }
    // funcionamento do codigo 
    public static void main(String[] args) {
        new ProdutoView().setVisible(true);
    }
}
