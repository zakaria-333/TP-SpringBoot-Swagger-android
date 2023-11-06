package ma.projet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import ma.projet.dao.IDao;
import ma.projet.entities.Role;
import ma.projet.repository.RoleRepository;

@Service
public class RoleService implements IDao<Role>{
	@Autowired
	private RoleRepository RoleRepository;

	@Override
	public Role create(Role o) {
		return RoleRepository.save(o);
	}

	@Override
	public boolean delete(Role o) {
		try {
			RoleRepository.delete(o);
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}

	@Override
    public Role update(Long id, Role o) {
        Role existingRole = RoleRepository.findById(id).orElse(null);

        if (existingRole != null) {
            try {
                existingRole.setName(o.getName());
                return RoleRepository.save(existingRole);
            } catch (DataAccessException e) {
                e.printStackTrace(); 
            }
        }
        return null;
    }

	@Override
	public List<Role> findAll() {
		
		return RoleRepository.findAll();
	}

	@Override
	public Role findById(Long id) {
		return RoleRepository.findById(id).orElse(null);
	}

}
