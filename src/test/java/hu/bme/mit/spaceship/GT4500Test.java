package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GT4500Test {

  private GT4500 ship;
  TorpedoStore ts1 = mock(TorpedoStore.class);
  TorpedoStore ts2 = mock(TorpedoStore.class);

  @BeforeEach
  public void init(){

    this.ship = new GT4500(ts1, ts2);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(1);
    when(ts2.getTorpedoCount()).thenReturn(0);
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(1);
    when(ts2.getTorpedoCount()).thenReturn(1);
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertTrue(result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Fail() {
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(0);
    when(ts2.getTorpedoCount()).thenReturn(0);
    when(ts1.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));
    when(ts2.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Fail_All_Empty(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(0);
    when(ts2.getTorpedoCount()).thenReturn(0);
    when(ts1.isEmpty()).thenReturn(true);
    when(ts2.isEmpty()).thenReturn(true);
    when(ts1.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));
    when(ts2.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(ts1, times(0)).fire(1);
    verify(ts2, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Fail_Primary_Empty(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(0);
    when(ts2.getTorpedoCount()).thenReturn(1);
    when(ts1.isEmpty()).thenReturn(true);
    when(ts2.isEmpty()).thenReturn(false);
    when(ts1.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(ts1, times(0)).fire(1);
    verify(ts2, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Fail_Secondary_Empty(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(1);
    when(ts2.getTorpedoCount()).thenReturn(0);
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(true);
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(ts1, times(0)).fire(1);
    verify(ts2, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Fallback_To_Secondary(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(0);
    when(ts2.getTorpedoCount()).thenReturn(1);
    when(ts1.isEmpty()).thenReturn(true);
    when(ts2.isEmpty()).thenReturn(false);
    when(ts1.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }
  @Test
  public void fireTorpedo_Single_Fallback_To_Primary(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(2);
    when(ts2.getTorpedoCount()).thenReturn(0);
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(true);
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenThrow(new IllegalArgumentException("numberOfTorpedos"));

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE)
            && ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(ts1, times(2)).fire(1);
    verify(ts2, times(1)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Primary_Fail(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(1);
    when(ts2.getTorpedoCount()).thenReturn(1);
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(false);
    when(ts1.fire(1)).thenReturn(false);
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Secondary_Fail(){
    // Arrange
    when(ts1.getTorpedoCount()).thenReturn(1);
    when(ts2.getTorpedoCount()).thenReturn(1);
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(false);
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }

}
