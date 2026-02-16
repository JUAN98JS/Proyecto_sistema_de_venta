package sistemaventa.service.impl;

import org.springframework.stereotype.Service;
import sistemaventa.model.Venta;
import sistemaventa.repository.VentaRepository;
import sistemaventa.service.VentaService;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> obtenerTodas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> obtenerPorId(String id) {
        return ventaRepository.findById(id);
    }
}