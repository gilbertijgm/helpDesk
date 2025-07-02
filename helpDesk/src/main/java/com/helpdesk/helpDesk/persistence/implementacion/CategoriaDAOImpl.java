package com.helpdesk.helpDesk.persistence.implementacion;

import com.helpdesk.helpDesk.entities.Categoria;
import com.helpdesk.helpDesk.persistence.ICategoriaDAO;
import com.helpdesk.helpDesk.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoriaDAOImpl implements ICategoriaDAO {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> categorias() {
       List<Categoria> lista = categoriaRepository.findAll();

        return lista;
    }

    @Override
    public Optional<Categoria> categoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public Categoria guardarCategoria(Categoria task) {
        return categoriaRepository.save(task);
    }


    @Override
    public void deleteCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}
