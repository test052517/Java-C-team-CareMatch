-- Animals
ALTER TABLE `Animals`
  ADD CONSTRAINT `fk_animals_kind`    FOREIGN KEY (`kind_id`)    REFERENCES `Species`(`kind_id`)    ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_animals_shelter` FOREIGN KEY (`shelter_id`) REFERENCES `Shelter`(`shelter_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_animals_image`   FOREIGN KEY (`image_id`)   REFERENCES `Image`(`image_id`)     ON DELETE SET NULL  ON UPDATE CASCADE;

-- Answer
ALTER TABLE `Answer`
  ADD CONSTRAINT `fk_answer_inquiry` FOREIGN KEY (`inquiry_id`) REFERENCES `Inquiry`(`inquiry_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_answer_user`    FOREIGN KEY (`user_id`)    REFERENCES `Users`(`user_id`)     ON DELETE RESTRICT ON UPDATE CASCADE;

-- Review
ALTER TABLE `Review`
  ADD CONSTRAINT `fk_review_image`  FOREIGN KEY (`image_id`)  REFERENCES `Image`(`image_id`)   ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_review_user`   FOREIGN KEY (`user_id`)   REFERENCES `Users`(`user_id`)    ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_review_animal` FOREIGN KEY (`animal_id`) REFERENCES `Animals`(`animal_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- Comment
ALTER TABLE `comment`
  ADD CONSTRAINT `fk_comment_review` FOREIGN KEY (`review_id`) REFERENCES `Review`(`review_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_comment_user`   FOREIGN KEY (`user_id`)   REFERENCES `Users`(`user_id`)    ON DELETE RESTRICT ON UPDATE CASCADE;

-- Visit
ALTER TABLE `Visit`
  ADD CONSTRAINT `fk_visit_user`        FOREIGN KEY (`user_id`)        REFERENCES `Users`(`user_id`)           ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_visit_animal`      FOREIGN KEY (`animal_id`)      REFERENCES `Animals`(`animal_id`)       ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_visit_application` FOREIGN KEY (`application_id`) REFERENCES `Adoption`(`application_id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ChatMessage
ALTER TABLE `ChatMessage`
  ADD CONSTRAINT `fk_chatmessage_room`  FOREIGN KEY (`room_id`)  REFERENCES `ChattingRoom`(`room_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_chatmessage_user`  FOREIGN KEY (`user_id`)  REFERENCES `Users`(`user_id`)        ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_chatmessage_image` FOREIGN KEY (`image_id`) REFERENCES `Image`(`image_id`)       ON DELETE SET NULL ON UPDATE CASCADE;

-- Favorites
ALTER TABLE `fav_animal`
  ADD CONSTRAINT `fk_fav_user`   FOREIGN KEY (`user_id`)   REFERENCES `Users`(`user_id`)     ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_fav_animal` FOREIGN KEY (`animal_id`) REFERENCES `Animals`(`animal_id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `fav_animal` ADD UNIQUE KEY uq_fav (`user_id`,`animal_id`);
