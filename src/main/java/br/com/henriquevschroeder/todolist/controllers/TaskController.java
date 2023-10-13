package br.com.henriquevschroeder.todolist.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.henriquevschroeder.todolist.models.TaskModel;
import br.com.henriquevschroeder.todolist.repositories.ITaskRepository;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request)
    {
        var userId = request.getAttribute("userId");

        taskModel.setUserId((UUID) userId);

        TaskModel createdTask = this.taskRepository.save(taskModel);

        String locationHeader = "/tasks/" + createdTask.getId();
        
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", locationHeader).build();
    }

}
