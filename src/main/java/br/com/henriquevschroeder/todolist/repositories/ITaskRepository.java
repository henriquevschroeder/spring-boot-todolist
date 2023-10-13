package br.com.henriquevschroeder.todolist.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.henriquevschroeder.todolist.models.TaskModel;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {}
