import java.util.Scanner;

public interface FuncionalidadesEspecificas {
    void mostrarMenu();
    void executarAcao(int opcao, Scanner scanner);
    String getTipo();
}