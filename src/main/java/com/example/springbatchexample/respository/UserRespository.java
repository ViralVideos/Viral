package com.example.springbatchexample.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springbatchexample.model.User;

public interface UserRespository extends JpaRepository<User,Integer> {

	void saveAndFlush(List<? extends User> users);

}
