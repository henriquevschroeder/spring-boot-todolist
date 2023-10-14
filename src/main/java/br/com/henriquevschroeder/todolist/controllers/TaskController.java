package br.com.henriquevschroeder.todolist.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public List<TaskModel> list(HttpServletRequest request)
    {
        var userId = request.getAttribute("userId");

        var tasks = this.taskRepository.findByUserId((UUID) userId);

        return tasks;
    }
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request)
    {
        var userId = request.getAttribute("userId");

        taskModel.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getFinishAt())) {
            return ResponseEntity.badRequest().body("Start or finish date must be greater than current date");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getFinishAt())) {
            return ResponseEntity.badRequest().body("Start date must be before finish date");
        }

        TaskModel createdTask = this.taskRepository.save(taskModel);

        String locationHeader = "/tasks/" + createdTask.getId();
        
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", locationHeader).build();
    }

}
