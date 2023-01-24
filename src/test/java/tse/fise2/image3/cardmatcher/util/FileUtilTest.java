package tse.fise2.image3.cardmatcher.util;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {
    private final String testFolder = "test_folder";
    private final String testFile = "test_file";
    private final String testExt = "txt";

    @Test
    void createFolder() {

        FileUtil.CreateFolder(testFolder);
        File file = new File(testFolder);
        assertTrue(file.exists()); // vérifie que le dossier a bien été créé
        file.delete(); // supprime le dossier pour ne pas laisser de résidus
    }

    @Test
    void createFile() {
        FileUtil.CreateFile(testFile, testExt);
        File file = new File(testFile + "." + testExt);
        assertTrue(file.exists()); // vérifie que le fichier a bien été créé
        file.delete(); // supprime le fichier pour ne pas laisser de résidus
    }

    @Test
    void copyfolder() throws IOException {
        // Création d'un répertoire de test
        String sourceFolder = "source_folder";
        FileUtil.CreateFolder(sourceFolder);

        // Création d'un fichier à l'intérieur du répertoire de test
        String testFile = "test_file";
        FileUtil.CreateFile(sourceFolder + "/" + testFile, testExt);

        // Appel de la fonction copyfolder
        FileUtil.copyfolder(sourceFolder);

        // Vérifications
        String userHome = System.getProperty("user.dir");
        String targetFolder = userHome + "/apprentissage";
        File copiedFolder = new File(userHome + "/" + sourceFolder);
        File copiedFile = new File(targetFolder + "/" + testFile + "." + testExt);
        assertTrue(copiedFile.exists()); // vérifie que le fichier a été copié dans le répertoire d'apprentissage

        // Suppression des répertoires et fichiers créés pour ne pas laisser de résidus
        FileUtils.deleteDirectory(new File(sourceFolder));
        FileUtils.deleteDirectory(copiedFolder);
    }

    @Test
    void copyfile() throws IOException {
        FileUtil.CreateFile(testFile, testExt);
        FileUtil.copyfile(testFile + "." + testExt);
        String userHome = System.getProperty("user.dir");
        File copiedFile = new File(userHome + "/apprentissage/" + testFile + "." + testExt);
        assertTrue(copiedFile.exists()); // vérifie que le fichier a été copié dans le répertoire d'apprentissage
        new File(testFile + "." + testExt).delete();
        copiedFile.delete();
    }
}
