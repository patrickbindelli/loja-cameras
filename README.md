# Loja de Câmeras

## Visão Geral
Projeto desenvolvido para disciplina de Programção de Computadores III  
Programa para controle de estoque, venda, compra de estoque e fechamento de caixa.

## Editando os arquivos FXML
Esse programa foi desenvolvido utilizando a biblioteca de icones Ikonli, com seu plugin para o pacote de ícones FontAwesome,  
portanto, para coneguir editar os arqquivos utilizando o SceneBuider, são necessários alguns passos

Em Library -> Ícone de engrenagem -> JAR/FXML Manager -> Search repositories, adicione os seguintes:

```
org.kordamp.ikonli:ikonli-core
org.kordamp.ikonli:ikonli-javafx
org.kordamp.ikonli:ikonli-fontawesome5-pack
```

## Utilização
Necessário: JDK 11, Intellij

1. Clone esse repositório

2. Restaure o banco de dados no PGAdmin, utilizando o arquivo disponibilizado

3. Edite as configurações de conexão na classe DaoPostgres.java

4. Execute o programa pela classe Application.java

## Diagramas de Classe e Casos de Uso


![Diagrama de Classes](https://raw.githubusercontent.com/patrickbindelli/loja-cameras/master/diagramas/lojaCameraClassDiagram.png)
![Diagrama de Casos de Uso](https://raw.githubusercontent.com/patrickbindelli/loja-cameras/master/diagramas/lojaCameraUseCaseDiagram.png)
