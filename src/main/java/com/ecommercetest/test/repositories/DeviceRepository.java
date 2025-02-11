package com.ecommercetest.test.repositories;

import com.ecommercetest.test.domain.device.Device;
import com.ecommercetest.test.domain.device.DeviceState;
import com.ecommercetest.test.domain.device.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * Cosa fa la query (parte SELECT):
     *  1.	FROM device d
     *      La query parte dalla tabella device e la rinomina con l’alias d.
     *	2.	WHERE (:model IS NULL OR LOWER(d.model) LIKE LOWER(CONCAT(’%’, :model, ‘%’)))
     *      •	:model IS NULL:
     *      Se il parametro model è NULL, questa parte della condizione risulta vera per tutti i record. In altre parole, se non viene passato un valore per model, non viene applicato alcun filtro sul campo model.
     *	    •	OR LOWER(d.model) LIKE LOWER(CONCAT('%', :model, '%')):
     *      Se invece model non è NULL, viene convertito in minuscolo e confrontato con il campo model della tabella (anch’esso convertito in minuscolo) usando l’operatore LIKE.
     * 	    •	CONCAT('%', :model, '%') crea una stringa che racchiude il valore passato da percentuali, ad esempio se model è "iphone", il risultato è "%iphone%".
     * 	    •	LOWER(...) su entrambi i lati rende la ricerca case-insensitive.
     *      In sostanza, questa condizione seleziona tutti i record in cui il campo d.model contiene il valore del parametro model.
     *	3.	AND (:state IS NULL OR d.state = CAST(:state AS integer))
     *	    •	:state IS NULL:
     *      Se il parametro state è NULL, questa condizione è vera per tutti i record, quindi non applica alcun filtro sullo stato.
     * 	    •	OR d.state = CAST(:state AS integer):
     *      Se state non è NULL, il parametro viene convertito (CAST) a integer e confrontato con il campo d.state.
     *      Questo presuppone che la colonna state nel database sia di tipo numerico (ad esempio, rappresenta l’ordinal dell’enum) o comunque compatibile con un valore intero.
     *      Se il parametro è passato come numero (ad esempio 1, 2, ecc.), questa condizione filtra i record dove d.state è uguale a quel valore.
     *	4.	AND (:type IS NULL OR d.type = CAST(:type AS integer))
     *	    •	Simile al controllo sullo stato:
     *      Se il parametro type è NULL, la condizione è vera per tutti i record.
     *      Se invece non è NULL, il parametro viene convertito in intero tramite CAST(:type AS integer) e confrontato con il campo d.type.
     *      Questa parte filtra i record in base al tipo, supponendo che la colonna type sia di tipo numerico.
     *  Count Query
     *      La countQuery è sostanzialmente identica alla query principale, ma utilizza SELECT count(*) anziché SELECT *. Questo serve a determinare il numero totale di record corrispondenti ai criteri di filtraggio, essenziale per la paginazione.
     *  Come funziona nel contesto della paginazione:
     *	•	Il parametro Pageable passato al metodo gestisce la paginazione (ad esempio, impostando LIMIT e OFFSET nella query finale, a seconda della configurazione di Spring Data).
     * 	•	La query nativa verrà eseguita con i parametri:
     * 	•	Se model è NULL, la condizione sul modello verrà ignorata.
     * 	•	Se state è NULL, non verrà applicato alcun filtro sullo stato; altrimenti verrà confrontato l’intero passato.
     * 	•	Lo stesso per type.
     * In pratica, questa query filtra dinamicamente in base ai valori passati:
     * 	•	Se un parametro è NULL, il filtro relativo viene saltato.
     * 	•	Se è valorizzato, il filtro viene applicato usando una condizione case-insensitive per il modello e un confronto numerico per state e type.
     */
    @Query(
            value = "SELECT * FROM device d " +
                    "WHERE (:model IS NULL OR LOWER(d.model) LIKE LOWER(CONCAT('%', :model, '%'))) " +
                    "AND (:state IS NULL OR d.state = CAST(:state AS integer)) " +
                    "AND (:type IS NULL OR d.type = CAST(:type AS integer))",
            countQuery = "SELECT count(*) FROM device d " +
                    "WHERE (:model IS NULL OR LOWER(d.model) LIKE LOWER(CONCAT('%', :model, '%'))) " +
                    "AND (:state IS NULL OR d.state = CAST(:state AS integer)) " +
                    "AND (:type IS NULL OR d.type = CAST(:type AS integer))",
            nativeQuery = true
    )
    Page<Device> findDevicesByFilters(Pageable pageable,
                                      @Param("model") String model,
                                      @Param("state") Integer state,
                                      @Param("type") Integer type);
}
