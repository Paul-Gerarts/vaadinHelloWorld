package be.juvo.paul.battleship.services;

import be.juvo.paul.battleship.entities.ComputerShot;
import be.juvo.paul.battleship.repositories.ComputerShotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComputerShotServiceImpl {

    private ComputerShotRepository computerShotRepository;

    @Autowired
    public ComputerShotServiceImpl(
            ComputerShotRepository computerShotRepository
    ) {
        this.computerShotRepository = computerShotRepository;
    }

    public List<ComputerShot> findAllSortedByIdDesc() {
        return computerShotRepository.findAll().stream().sorted((el1, el2) -> el2.getId().compareTo(el1.getId())).collect(Collectors.toList());
    }

    public void deleteAll() {
        computerShotRepository.deleteAll();
    }

    public void save(ComputerShot shot) {
        computerShotRepository.save(shot);
    }
}
