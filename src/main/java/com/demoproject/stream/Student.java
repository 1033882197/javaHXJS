package com.demoproject.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description: Student <br>
 * @date: 2022/3/14 15:41 <br>
 * @author: Rile <br>
 * @version: 1.0 <br>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {
    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "年龄：" + age + ", 姓名：" + name;
    }
}
