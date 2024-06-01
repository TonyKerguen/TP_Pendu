import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;

/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    private Stage stage;
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Info / Rond avec un I dedans
     */
    private Button boutonInfo;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */
    private Button boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */
    private Button bJouer;

    private Color couleurTop;

    private HBox banniere;

    private Rectangle rectangleCouleur;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono
     * ...)
     */
    @Override
    public void init() {
        this.couleurTop = Color.valueOf("deb887");

        this.modelePendu = new MotMystere("./dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");

        ImageView imageHome = new ImageView(new Image("file:img/home.png", 50, 50, true, true));
        this.boutonMaison = new Button("", imageHome);
        RetourAccueil retourAccueil = new RetourAccueil(modelePendu, this);
        this.boutonMaison.setOnAction(retourAccueil);
        this.desacBoutonAccueil();

        ImageView imageParam = new ImageView(new Image("file:img/parametres.png", 50, 50, true, true));
        this.boutonParametres = new Button("", imageParam);
        ControleurParametres controleurParametres = new ControleurParametres(this);
        this.boutonParametres.setOnAction(controleurParametres);

        ImageView imageInfo = new ImageView(new Image("file:img/info.png", 50, 50, true, true));
        this.boutonInfo = new Button("", imageInfo);

        this.bJouer = new Button("Lancer une partie");
        ControleurLancerPartie controleurLancerPartie = new ControleurLancerPartie(modelePendu, this);
        this.bJouer.setOnAction(controleurLancerPartie);

        this.niveaux = Arrays.asList("Facile", "Médium", "Difficile", "Expert");

        this.motCrypte = new Text(modelePendu.getMotCrypte());
        motCrypte.setFont(Font.font("Arial", FontWeight.NORMAL, 28));

        this.dessin = new ImageView(new Image("file:./img/pendu0.png"));


        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(this.modelePendu, this), couleurTop);
        this.clavier.setMaxWidth(550);

        this.pg = new ProgressBar(0);

        this.leNiveau = new Text("Niveau " + modelePendu.nomNiveau());

        this.chrono = new Chronometre();

        // A terminer d'implementer
    }

    /**
     * @return le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene() {
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private HBox titre() {
        this.banniere = new HBox();
        Label titre = new Label("Jeu du Pendu");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);
        banniere.getChildren().addAll(titre, espace, boutonMaison, boutonParametres, boutonInfo);
        banniere.setBackground(new Background(new BackgroundFill(couleurTop, CornerRadii.EMPTY, Insets.EMPTY)));
        banniere.setSpacing(2);
        banniere.setPadding(new Insets(15));
        banniere.setAlignment(Pos.CENTER);
        return banniere;
    }

    // /**
    // * @return le panel du chronomètre
    // */
    // private TitledPane leChrono(){
    // A implementer
    // TitledPane res = new TitledPane();
    // return res;
    // }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private BorderPane fenetreJeu() {
        // Milieu
        BorderPane pane = new BorderPane();
        VBox centre = new VBox();
        for(Button b : this.clavier.getClavier()){
            b.setStyle(
                "-fx-background-color: #"+couleurTop.toString().substring(2, couleurTop.toString().length() - 2)+";" +    // Couleur de fond
                "-fx-text-fill: black;" +             // Couleur du texte
                "-fx-font-size: 16px;" +              // Taille de la police
                "-fx-padding: 10 20;" +               // Padding interne
                "-fx-border-radius: 5;" +            // Bordure arrondie
                "-fx-background-radius: 5;"          // Fond arrondi
                );
        }
        this.pg.setStyle("-fx-accent: #"+this.couleurTop.toString().substring(2, couleurTop.toString().length() - 2)+";");
        centre.getChildren().addAll(motCrypte, dessin, pg, clavier);
        centre.setAlignment(Pos.TOP_CENTER);
        centre.setSpacing(10);
        pane.setCenter(centre);

        // Droite
        VBox droite = new VBox();
        leNiveau.setText("Niveau " + this.niveaux.get(this.modelePendu.getNiveau()));
        leNiveau.setFont(new Font(20));
        TitledPane chrono = new TitledPane("Chronomètres", this.chrono);

        chrono.setCollapsible(false);
        droite.setAlignment(Pos.CENTER);
        bJouer.setText("Nouveau mot");
        droite.getChildren().addAll(leNiveau, chrono, bJouer);
        droite.setPadding(new Insets(10, 70, 0, 0));
        droite.setSpacing(10);
        droite.setAlignment(Pos.TOP_CENTER);
        pane.setRight(droite);
        return pane;
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de
     *         jeu
     */
    private BorderPane fenetreAccueil() {
        BorderPane pane = new BorderPane();
        VBox vbox = new VBox();
        VBox difficulte = new VBox();
        ToggleGroup group = new ToggleGroup();
        boolean d = false;
        for (String s : niveaux) {
            RadioButton r = new RadioButton(s);
            r.setToggleGroup(group);
            r.setOnAction(new ControleurNiveau(modelePendu));
            difficulte.getChildren().add(r);
            if (!d) {
                r.setSelected(true);
                d = true;
            }
        }
        TitledPane titledPane = new TitledPane("Niveau de difficulté", difficulte);
        titledPane.setCollapsible(false);
        bJouer.setText("Lancer une partie");
        vbox.getChildren().addAll(bJouer, titledPane);
        vbox.setSpacing(10);
        difficulte.setSpacing(10);
        pane.setCenter(vbox);
        pane.setPadding(new Insets(15));
        return pane;
    }

    private BorderPane fenetreParametre() {
        BorderPane pane = new BorderPane();
        HBox hbox = new HBox();
        ColorPicker choixCouleur=new ColorPicker(Color.web(this.couleurTop.toString()));
        choixCouleur.setOnAction(new ControleurCouleur(this, choixCouleur));
        this.rectangleCouleur = new Rectangle(100, 100);
        rectangleCouleur.setFill(this.couleurTop);
        hbox.getChildren().addAll(choixCouleur, rectangleCouleur);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER_LEFT);
        pane.setTop(hbox);
        return pane;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * 
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire) {
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil() {
        this.desacBoutonAccueil();
        this.activerBoutonParametre();
        this.panelCentral = fenetreAccueil();
        stage.setScene(this.laScene());
    }

    public void modeJeu() {
        this.desacBoutonParametre();
        this.panelCentral = fenetreJeu();
        stage.setScene(this.laScene());
        this.getChrono().resetTime();
        this.chrono.start();
    }

    public void modeParametres() {
        this.desacBoutonParametre();
        this.activerBoutonAccueil();
        this.panelCentral = fenetreParametre();
        stage.setScene(this.laScene());
    }

    /** lance une partie */
    public void lancePartie() {
        // A implementer
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));
        this.pg.setProgress((this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()) * 0.1);
        if (this.modelePendu.gagne()) {
            if (this.popUpMessageGagne().showAndWait().get().equals(ButtonType.YES)){
                this.getChrono().resetTime();
                this.getChrono().start();
                this.modelePendu.setMotATrouver();
                this.getClavier().desactiveTouches(this.modelePendu.getLettresEssayees());
                this.majAffichage();
            }
            else{
                this.modeAccueil();
            }
        }
        if (this.modelePendu.perdu()) {
            if (this.popUpMessagePerdu().showAndWait().get().equals(ButtonType.YES)){
                this.getChrono().resetTime();
                this.getChrono().start();
                this.modelePendu.setMotATrouver();
                this.getClavier().desactiveTouches(this.modelePendu.getLettresEssayees());
                this.majAffichage();
            }
            else{
                this.modeAccueil();
            }
        }
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * 
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono() {
        return this.chrono;
    }

    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    public Alert popUpReglesDuJeu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessageGagne() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez vous rejouez ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Jeu du Pendu");
        alert.setHeaderText("Vous avez gagné !");
        return alert;
    }

    public Alert popUpMessagePerdu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Le mot à trouver était " + this.modelePendu.getMotATrouve() + "\nVoulez vous rejouez ?", ButtonType.NO, ButtonType.YES);
        alert.setTitle("Jeu du Pendu");
        alert.setHeaderText("Vous avez perdu :(");
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * 
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        this.modeAccueil();
        stage.setScene(this.laScene());
        stage.show();
    }

    public void desacBoutonAccueil() {
        this.boutonMaison.setDisable(true);
    }

    public void activerBoutonAccueil() {
        this.boutonMaison.setDisable(false);
    }

    public void activerBoutonParametre() {
        this.boutonParametres.setDisable(false);
    }

    public void desacBoutonParametre() {
        this.boutonParametres.setDisable(true);
    }

    public Clavier getClavier() {
        return this.clavier;
    }

    public void setCouleur(Color couleur) {
        this.couleurTop = couleur;
    }

    public HBox getBanniere() {
        return this.banniere;
    }

    public Rectangle getRectangleCouleur() {
        return this.rectangleCouleur;
    }

    public Button getBouttonLancerPartie() {
        return this.bJouer;
    }
    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }
}
