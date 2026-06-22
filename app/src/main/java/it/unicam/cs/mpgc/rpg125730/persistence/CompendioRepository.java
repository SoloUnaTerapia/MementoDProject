package it.unicam.cs.mpgc.rpg125730.persistence;

import it.unicam.cs.mpgc.rpg125730.model.Shadow;
import it.unicam.cs.mpgc.rpg125730.persistence.entity.DiscoveredShadowEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

/**
 * Gestisce l'interazione con il database globale (SQLite tramite Hibernate)
 * Si occupa di registrare le ombre incontrate nel Compendio
 */
public class CompendioRepository {

    private final EntityManagerFactory emf;

    public CompendioRepository() {
        this.emf = Persistence.createEntityManagerFactory("Compendio");
    }

    /**
     * Registra un'Ombra appena incontrata nel database
     * Se l'Ombra esiste già incrementa solo il contatore degli incontri
     */
    public void registerEncounter(Shadow shadow) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            //cerca nel db se abbiamo già incontrato questa ombra
            DiscoveredShadowEntity existingRecord = em.find(DiscoveredShadowEntity.class, shadow.getName());

            if (existingRecord == null) {
                //nuova scoperta
                String weak = shadow.getWeakness() != null ? shadow.getWeakness().name() : "Nessuna";
                String resist = shadow.getResistance() != null ? shadow.getResistance().name() : "Nessuna";

                DiscoveredShadowEntity newRecord = new DiscoveredShadowEntity(shadow.getName(), weak, resist);
                em.persist(newRecord);
            } else {
                //se già scoperta incrementiamo il contatore
                existingRecord.incrementEncounters();
                em.merge(existingRecord);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Errore durante l'aggiornamento del Compendio: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Recupera tutte le ombre scoperte dal db per mostrarle
     */
    public List<DiscoveredShadowEntity> getAllDiscoveredShadows() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT d FROM DiscoveredShadowEntity d", DiscoveredShadowEntity.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}