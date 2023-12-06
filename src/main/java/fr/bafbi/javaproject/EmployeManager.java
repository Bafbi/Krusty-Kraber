package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.*;

import java.util.*;

public class EmployeManager {

    private final List<Employe> employes = new ArrayList<>();
    private final Map<Integer, Boolean> equipe = new HashMap<>();

    public Employe getEmploye(int employeId) {
        return employes.get(employeId);
    }

    public Employe recruter(String nom, String prenom, double salaire, Class<? extends Employe> employeClass) {
        int id = employes.size();
        try {
            Employe employe = employeClass.getConstructor(String.class, String.class, double.class, int.class).newInstance(nom, prenom, salaire, id);
            employes.add(employe);
            equipe.put(id, false);
            return employe;
        } catch (Exception e) {
            Restaurant.getLogger().error("Impossible de créer un employé de type " + employeClass.getName(), e);
        }
        return null;
    }

    public void licencier(int employeId) {
        employes.remove(employeId);
        equipe.remove(employeId);
    }

    public void planifier(int employeId) {
        equipe.put(employeId, true);
    }

    public void planifier(Employe employe) {
        planifier(employe.getId());
    }

    public void annuler(int employeId) {
        equipe.put(employeId, false);
    }

    public boolean isPlanifie(int employeId) {
        return equipe.get(employeId);
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public <T extends Employe> Set<T> getEmployes(Class<T> employeClass) {
        Set<T> employes = new HashSet<>();
        for (Employe employe : this.employes) {
            if (employe.getClass().equals(employeClass)) {
                employes.add((T) employe);
            }
        }
        return employes;
    }


    /**
     * @return la liste des employés planifiés
     */
    public Set<Employe> getEquipe() {
        Set<Employe> equipe = new HashSet<>();
        for (Map.Entry<Integer, Boolean> entry : this.equipe.entrySet()) {
            if (entry.getValue()) {
                equipe.add(employes.get(entry.getKey()));
            }
        }
        return equipe;
    }

    /**
     * Vérifie qu'il y a au minimum un manager, un barman, trois cuisinier et deux serveur
     *
     * @return true si l'équipe est valide, false sinon
     */
    public boolean validerEquipe() {
        int nbManager = 0;
        int nbBarman = 0;
        int nbCuisinier = 0;
        int nbServeur = 0;
        for (Employe employe : getEquipe()) {
            if (employe instanceof Manager) {
                nbManager++;
            } else if (employe instanceof Barman) {
                nbBarman++;
            } else if (employe instanceof Cuisinier) {
                nbCuisinier++;
            } else if (employe instanceof Serveur) {
                nbServeur++;
            }
        }
        return nbManager >= 1 && nbBarman >= 1 && nbCuisinier >= 3 && nbServeur >= 2;
    }

    @Override
    public String toString() {
        return "EmployeManager{" +
                "employes=" + employes +
                ", equipe=" + equipe +
                '}';
    }
}
