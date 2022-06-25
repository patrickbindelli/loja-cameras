package br.edu.femass.testes;

import br.edu.femass.dao.VendaDao;
import br.edu.femass.model.Tipo;
import br.edu.femass.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MainTeste {
    public static void main(String[] args) {
        List<Venda> vendaList = new ArrayList<>();

        try {
            vendaList = new VendaDao().read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(vendaList);
    }

}
