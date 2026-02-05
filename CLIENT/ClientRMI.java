import java.rmi.Naming;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientRMI {
    public static void main(String[] args) {
        try {
            System.out.println("\n" + colorize("🚀 Initialisation de la connexion...", "CYAN"));
            
            // Animation de connexion
            System.out.print(colorize("📡 Connexion au serveur srv-calcul:1099", "BLUE"));
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(400);
            }
            
            Calculatrice stub = (Calculatrice) Naming.lookup("rmi://srv-calcul:1099/CalculService");
            System.out.println(colorize(" ✓", "GREEN"));
            
            Scanner sc = new Scanner(System.in);
            
            // Interface d'accueil
            printWelcomeBanner();
            
            System.out.println(colorize("\n📝 MODE CALCUL MULTIPLE", "YELLOW"));
            System.out.println("   Saisissez une suite d'opérations (ex: " + colorize("78 + 1651 - 50", "CYAN") + ")");
            System.out.println("   Opérateurs supportés: " + colorize("+  -  *  /", "GREEN"));
            System.out.println("   Tapez " + colorize("'exit'", "RED") + " pour quitter");
            System.out.println("   Tapez " + colorize("'help'", "BLUE") + " pour l'aide\n");
            System.out.println(colorize("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯", "PURPLE"));

            while (true) {
                System.out.print(colorize("\n➤ ", "GREEN") + colorize("Calcul: ", "CYAN"));
                String input = sc.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println(colorize("\n👋 Fermeture du client. À bientôt !", "YELLOW"));
                    break;
                }
                
                if (input.equalsIgnoreCase("help")) {
                    printHelp();
                    continue;
                }
                
                if (input.isEmpty()) {
                    System.out.println(colorize("⚠️  Veuillez saisir un calcul", "YELLOW"));
                    continue;
                }

                try {
                    // Afficher l'opération complète
                    System.out.println(colorize("   Opération: " + formatOperation(input), "BRIGHT_CYAN"));
                    
                    // Calcul en cours
                    System.out.print(colorize("   Calcul en cours", "BLUE"));
                    for (int i = 0; i < 3; i++) {
                        System.out.print(".");
                        Thread.sleep(200);
                    }
                    System.out.println();
                    
                    // Traitement avec StringTokenizer
                    StringTokenizer st = new StringTokenizer(input, "+-*/ ", true);
                    
                    if (!st.hasMoreTokens()) continue;

                    // Premier nombre
                    double resultat = Double.parseDouble(st.nextToken().trim());
                    StringBuilder historique = new StringBuilder(formatNumber(resultat));

                    while (st.hasMoreTokens()) {
                        String op = st.nextToken().trim();
                        if (op.isEmpty()) continue;

                        if (!st.hasMoreTokens()) break;
                        double suivant = Double.parseDouble(st.nextToken().trim());

                        // Enregistrer l'étape
                        historique.append(" ").append(colorize(op, "YELLOW")).append(" ").append(formatNumber(suivant));
                        
                        // Calcul distant via RMI
                        resultat = stub.calculer(resultat, suivant, op);
                    }

                    // Affichage du résultat
                    System.out.println(colorize("   ──────────────────────────────", "GREEN"));
                    System.out.println(colorize("   Historique: " + historique.toString(), "BRIGHT_BLUE"));
                    System.out.println(colorize("   Résultat final: " + colorize("= " + formatNumber(resultat), "BRIGHT_GREEN"), "WHITE"));
                    System.out.println(colorize("   ──────────────────────────────", "GREEN"));

                } catch (NumberFormatException e) {
                    System.out.println(colorize("❌ Erreur: Format de nombre invalide", "RED"));
                    System.out.println(colorize("   Vérifiez votre saisie (ex: 12.5 + 3)", "YELLOW"));
                } catch (ArithmeticException e) {
                    System.out.println(colorize("❌ Erreur mathématique: " + e.getMessage(), "RED"));
                } catch (Exception e) {
                    System.out.println(colorize("❌ Erreur: " + e.getMessage(), "RED"));
                }
            }
            
            sc.close();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println(colorize("\n❌ Impossible de se connecter au serveur RMI", "RED"));
            System.err.println(colorize("   Vérifiez que le serveur est démarré et accessible", "YELLOW"));
            e.printStackTrace();
        }
    }
    
    private static void printWelcomeBanner() {
        System.out.println("\n" + colorize("╔══════════════════════════════════════════════════╗", "CYAN"));
        System.out.println(colorize("║", "CYAN") + colorize("         🧮 CALCULATRICE RMI AVANCÉE          ", "BRIGHT_CYAN") + colorize("║", "CYAN"));
        System.out.println(colorize("║", "CYAN") + colorize("    Support des calculs multiples en chaîne   ", "CYAN") + colorize("║", "CYAN"));
        System.out.println(colorize("╚══════════════════════════════════════════════════╝", "CYAN"));
    }
    
    private static void printHelp() {
        System.out.println(colorize("\n📚 GUIDE D'UTILISATION", "YELLOW"));
        System.out.println(colorize("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯", "BLUE"));
        System.out.println(colorize("🎯 Exemples de calculs:", "GREEN"));
        System.out.println("   • " + colorize("10 + 5", "CYAN"));
        System.out.println("   • " + colorize("100 * 2 - 50 / 3", "CYAN"));
        System.out.println("   • " + colorize("15.5 + 4.2 * 3 - 10 / 2", "CYAN"));
        
        System.out.println(colorize("\n⚡ Priorité des opérations:", "GREEN"));
        System.out.println("   Les calculs sont traités " + colorize("de gauche à droite", "YELLOW"));
        System.out.println("   (sans priorité multiplicative)");
        
        System.out.println(colorize("\n💡 Conseils:", "BLUE"));
        System.out.println("   • Les espaces sont optionnels autour des opérateurs");
        System.out.println("   • Les nombres décimaux utilisent le point (.)");
        System.out.println("   • Tapez 'exit' pour quitter");
        System.out.println(colorize("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n", "BLUE"));
    }
    
    private static String formatOperation(String input) {
        // Nettoie et formate l'opération pour l'affichage
        String formatted = input
            .replace("+", " + ")
            .replace("-", " - ")
            .replace("*", " × ")
            .replace("/", " ÷ ")
            .replaceAll("\\s+", " ")
            .trim();
        
        return colorize(formatted, "BRIGHT_CYAN");
    }
    
    private static String formatNumber(double num) {
        // Formate le nombre pour éviter les .0 inutiles
        if (num == (long) num) {
            return String.format("%d", (long) num);
        } else {
            return String.format("%.4f", num).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }
    
    // Méthode pour colorer le texte
    private static String colorize(String text, String color) {
        if (System.console() == null || System.getProperty("os.name").toLowerCase().contains("win")) {
            // Sur Windows ou sans console, pas de couleurs par défaut
            return text;
        }
        
        String colorCode = "";
        switch (color.toUpperCase()) {
            case "BLACK": colorCode = "\u001B[30m"; break;
            case "RED": colorCode = "\u001B[31m"; break;
            case "GREEN": colorCode = "\u001B[32m"; break;
            case "YELLOW": colorCode = "\u001B[33m"; break;
            case "BLUE": colorCode = "\u001B[34m"; break;
            case "PURPLE": colorCode = "\u001B[35m"; break;
            case "CYAN": colorCode = "\u001B[36m"; break;
            case "WHITE": colorCode = "\u001B[37m"; break;
            case "BRIGHT_RED": colorCode = "\u001B[91m"; break;
            case "BRIGHT_GREEN": colorCode = "\u001B[92m"; break;
            case "BRIGHT_YELLOW": colorCode = "\u001B[93m"; break;
            case "BRIGHT_BLUE": colorCode = "\u001B[94m"; break;
            case "BRIGHT_PURPLE": colorCode = "\u001B[95m"; break;
            case "BRIGHT_CYAN": colorCode = "\u001B[96m"; break;
            case "BRIGHT_WHITE": colorCode = "\u001B[97m"; break;
            default: return text;
        }
        return colorCode + text + "\u001B[0m";
    }
}
