package com.kiendh.springsecurity.dto.response;

import com.kiendh.springsecurity.dto.enums.Gender;
import com.kiendh.springsecurity.dto.enums.Status;
import com.kiendh.springsecurity.entity.Customer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerResponse implements Serializable {

    private Long id;

    private String username;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String email;

    private String phoneNumber;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.username = customer.getUsername();
        this.fullName = customer.getFullName();
        this.gender = customer.getGender();
        this.dob = customer.getDob();
        this.status = customer.getStatus();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
    }

    public static Integer longestConsecutive(List<Integer> nums) {
        Set<Integer> set = new HashSet<>(nums);

        int longest = 0;

        for(Integer num : set) {
            if(!set.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                while(set.contains(currentNum + 1)) {
                    currentNum += 1;
                    currentStreak += 1;
                }

                longest = Math.max(longest, currentStreak);
            }
        }
        return longest;
    }
}
