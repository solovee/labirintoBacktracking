package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.core.utils.DrawingUtils;
import br.com.davidbuzatto.jsge.image.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends EngineFrame {

    int[][] labirinto;
    int[][] visitados;
    ArrayList<int[][]> caminho;
    ArrayList<Character[][]> direcoes;
    int inicioL, inicioC, fimL, fimC, pos;
    private double tempoParaMudar, contadorTempo;
    private boolean saida;
    private ArrayList<Character> direcoesPrioritarias;

    public Main() {
        super(650, 550, "Labirinto", 60, true, false, false, false, false, false);
    }

    @Override
    public void create() {
        caminho = new ArrayList<>();
        direcoes = new ArrayList<>();
        

        
        
        
        
        labirinto = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 0, 1, 3, 0, 1, 0, 0, 1, 0, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1},
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

        visitados = new int[labirinto.length][labirinto[0].length];

        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[i].length; j++) {
                if (labirinto[i][j] == 2) {
                    inicioL = i;
                    inicioC = j;
                }
                if (labirinto[i][j] == 3) {
                    fimL = i;
                    fimC = j;
                }
            }
        }

        saida = false;

        direcoesPrioritarias = new ArrayList<>(Arrays.asList('D', 'C', 'B', 'E'));

        movimenta(inicioL, inicioC, fimL, fimC, new Character[labirinto.length][labirinto[0].length]);
        pos = 0;
        tempoParaMudar = 0.15; 
    }
     private void drawGrid() {
        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[i].length; j++) {
                drawRectangle(50 * j, 50 * i, 50, 50, BLACK);
            }
        }
    }

    @Override
    public void update(double delta) {
        contadorTempo += delta;
        if (contadorTempo > tempoParaMudar && pos < caminho.size() - 1) {
            contadorTempo = 0;
            pos++;
            if (caminho.get(pos)[fimL][fimC] == 1) {
                saida = true;
            }
        }
    }

    @Override
    public void draw() {
        clearBackground(WHITE);
        
        
        
        
        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[i].length; j++) {
                switch (labirinto[i][j]) {
                    case 1 -> fillRectangle(50 * j, 50 * i, 50, 50, BLACK);
                    case 2 -> drawText("I", 50 * j + 25, 50 * i + 25, 12, BLACK);
                    case 3 -> drawText("F", 50 * j + 25, 50 * i + 25, 12, BLACK);
                }
            }
        }
        desenharAcessados(caminho.get(pos), direcoes.get(pos));

        if (saida) {
            fillRectangle(50 * fimC, 50 * fimL, 50, 50, BLUE);
            drawText("F", 50 * fimC + 25, 50 * fimL + 25, 12, WHITE);
            drawText("VITÓRIA!", 250, 20, 30, BLUE);
        }
        drawGrid();
    }

    private boolean movimenta(int linha, int coluna, int linhaDest, int colunaDest, Character[][] dire) {
        if (valido(linha, coluna)) {
            visitados[linha][coluna] = 1;
            caminho.add(copyMatrix(visitados));
            direcoes.add(copyMatrix(dire));

            if (linha == linhaDest && coluna == colunaDest) {
                return true;
            }

            for (char direcao : direcoesPrioritarias) {
                dire[linha][coluna] = direcao;
                caminho.add(copyMatrix(visitados));
                direcoes.add(copyMatrix(dire));

                switch (direcao) {
                    case 'D' -> {
                        if (movimenta(linha, coluna + 1, linhaDest, colunaDest, dire)) return true;
                    }
                    case 'C' -> {
                        if (movimenta(linha - 1, coluna, linhaDest, colunaDest, dire)) return true;
                    }
                    case 'B' -> {
                        if (movimenta(linha + 1, coluna, linhaDest, colunaDest, dire)) return true;
                    }
                    case 'E' -> {
                        if (movimenta(linha, coluna - 1, linhaDest, colunaDest, dire)) return true;
                    }


                }
            }
        }
        return false;
    }

    private boolean valido(int linha, int coluna) {
        return linha >= 0 && linha < labirinto.length && 
               coluna >= 0 && coluna < labirinto[0].length && 
               labirinto[linha][coluna] != 1 && 
               visitados[linha][coluna] != 1;
    }

    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    private Character[][] copyMatrix(Character[][] original) {
        Character[][] copy = new Character[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    private void desenharAcessados(int[][] acessado, Character[][] direcao) {
        for (int i = 0; i < acessado.length; i++) {
            for (int j = 0; j < acessado[i].length; j++) {
                if (acessado[i][j] == 1) {
                    fillRectangle(50 * j, 50 * i, 50, 50, GREEN);
                    if (direcao[i][j] != null) {
                        drawText(String.valueOf(direcao[i][j]), 50 * j + 25, 50 * i + 25, 12, BLACK);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
