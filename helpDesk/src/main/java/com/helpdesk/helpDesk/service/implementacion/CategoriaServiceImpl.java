package com.helpdesk.helpDesk.service.implementacion;

import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.entities.Categoria;
import com.helpdesk.helpDesk.exceptions.ResourceNotFoundException;
import com.helpdesk.helpDesk.exceptions.ServiceUtils;
import com.helpdesk.helpDesk.persistence.ICategoriaDAO;
import com.helpdesk.helpDesk.service.ICategoriaService;
import com.helpdesk.helpDesk.service.mappers.CategoriaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements ICategoriaService {

    private final ICategoriaDAO categoriaDAO;
    private final CategoriaMapper categoriaMapper;

    //Metodo para listar todos los recursos
    @Override
    public List<CategoriaDTO> categorias() {

        List<Categoria> lista = categoriaDAO.categorias();

        //validamos si esta vacia
        ServiceUtils.validateNotEmpty(lista, "No hay categorias registradas");

        return categoriaMapper.toDtoList(lista);
    }

    //Metodo para buescar recurso por id
    @Override
    public CategoriaDTO categoriaById(Long id) {
        return categoriaDAO.categoriaById(id)
                .map(categoriaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
    }

    //Metodo para guardar
    @Override
    public CategoriaDTO guardarCategoria(CategoriaDTO categoriaDTO) {

        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);

        categoria = categoriaDAO.guardarCategoria(categoria);

        return categoriaMapper.toDto(categoria);
    }

    //Metodo para eliminar
    @Override
    public void deleteCategoria(Long id) {
        //buscamos la tarea a eliminar por el id recibido
        Categoria categoria = categoriaDAO.categoriaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));

        categoriaDAO.deleteCategoria(categoria.getIdCategoria());
    }
}
