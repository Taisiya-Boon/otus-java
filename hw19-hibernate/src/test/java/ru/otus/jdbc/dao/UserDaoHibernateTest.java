package ru.otus.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.AbstractHibernateTest;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с пользователями должно ")
class UserDaoHibernateTest extends AbstractHibernateTest {

  private SessionManagerHibernate sessionManagerHibernate;
  private UserDaoHibernate userDaoHibernate;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
    userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
  }

  @Test
  @DisplayName(" корректно загружать пользователя по заданному id")
  void shouldFindCorrectUserById() {
      AddressDataSet addressDataSet = new AddressDataSet(0, "Улица Ленина");
      PhoneDataSet phoneDataSet1 = new PhoneDataSet(0, "114-34-56");
      PhoneDataSet phoneDataSet2 = new PhoneDataSet(0, "225-73-68");
      List<PhoneDataSet> phoneDataSets = new ArrayList<>();
      phoneDataSets.add(phoneDataSet1);
      phoneDataSets.add(phoneDataSet2);

      User expectedUser = new User(0, "Вася", addressDataSet, phoneDataSets);
      saveUser(expectedUser);

      assertThat(expectedUser.getId()).isGreaterThan(0);

      sessionManagerHibernate.beginSession();
      Optional<User> mayBeUser = userDaoHibernate.findById(expectedUser.getId());
      sessionManagerHibernate.commitSession();

      assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByFieldRecursively(expectedUser);
  }

  @DisplayName(" корректно сохранять пользователя")
  @Test
  void shouldCorrectSaveUser() {
      AddressDataSet addressDataSet = new AddressDataSet(0, "Улица Ленина");
      PhoneDataSet phoneDataSet1 = new PhoneDataSet(0, "114-34-56");
      PhoneDataSet phoneDataSet2 = new PhoneDataSet(0, "225-73-68");
      List<PhoneDataSet> phoneDataSets = new ArrayList<>();
      phoneDataSets.add(phoneDataSet1);
      phoneDataSets.add(phoneDataSet2);

      User expectedUser = new User(0, "Вася", addressDataSet, phoneDataSets);
      sessionManagerHibernate.beginSession();
      long id = userDaoHibernate.saveUser(expectedUser);
      sessionManagerHibernate.commitSession();

      assertThat(id).isGreaterThan(0);

      User actualUser = loadUser(id);
      assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

      expectedUser = new User(id, "Не Вася");
      sessionManagerHibernate.beginSession();
      long newId = userDaoHibernate.saveUser(expectedUser);
      sessionManagerHibernate.commitSession();

      assertThat(newId).isGreaterThan(0).isEqualTo(id);
      actualUser = loadUser(newId);
      assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

  }

  @DisplayName(" возвращать менеджер сессий")
  @Test
  void getSessionManager() {
    assertThat(userDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
  }

}
