package edu.iis.mto.testreactor.washingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WashingMachineTest {

    @Mock
    private DirtDetector dirtDetector;
    @Mock
    private Engine engine;
    @Mock
    private WaterPump waterPump;
    private WashingMachine washingMachine;

    @BeforeEach
    void setUp() {
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);
    }

    @Test
    void laundryCompletedSuccessfullyTest() {
        Material material = Material.WOOL;
        Program program = Program.SHORT;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(1).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.SUCCESS).withErrorCode(ErrorCode.NO_ERROR).withRunnedProgram(Program.SHORT).build();
        assertEquals(laundryReturnStatus, washingMachine.start(laundryBatch,programConfiguration));
    }

    @Test
    void laundryFailedWithTooHeavyError(){
        Material material = Material.JEANS;
        Program program = Program.MEDIUM;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(11).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.FAILURE).withErrorCode(ErrorCode.TOO_HEAVY).withRunnedProgram(null).build();
        assertEquals(laundryReturnStatus, washingMachine.start(laundryBatch,programConfiguration));
    }

    @Test
    void waterPumErrorSoLaundryFailed() throws WaterPumpException {
       doThrow(new WaterPumpException()).when(waterPump).pour(any(double.class));
        Material material = Material.JEANS;
        Program program = Program.MEDIUM;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(1).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.FAILURE).withErrorCode(ErrorCode.WATER_PUMP_FAILURE).withRunnedProgram(Program.MEDIUM).build();
        assertEquals(laundryReturnStatus, washingMachine.start(laundryBatch,programConfiguration));
    }
    @Test
    void engineErrorSoLaundryFailed() throws EngineException {
        doThrow(new EngineException()).when(engine).runWashing(any(int.class));
        Material material = Material.JEANS;
        Program program = Program.LONG;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(1).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.FAILURE).withErrorCode(ErrorCode.ENGINE_FAILURE).withRunnedProgram(Program.LONG).build();
        assertEquals(laundryReturnStatus, washingMachine.start(laundryBatch,programConfiguration));
    }
    @Test
    void dirtDetectorSetProgramToLong() throws Exception {
        when(dirtDetector.detectDirtDegree(any(LaundryBatch.class))).thenReturn(new Percentage(60));
        Material material = Material.JEANS;
        Program program = Program.AUTODETECT;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(2).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.SUCCESS).withErrorCode(ErrorCode.NO_ERROR).withRunnedProgram(Program.LONG).build();
        assertEquals(laundryReturnStatus, washingMachine.start(laundryBatch,programConfiguration));
    }

    @Test
    void dirtDetectorSetProgramToMedium() throws Exception {
        when(dirtDetector.detectDirtDegree(any(LaundryBatch.class))).thenReturn(new Percentage(12));
        Material material = Material.JEANS;
        Program program = Program.AUTODETECT;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(2).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.SUCCESS).withErrorCode(ErrorCode.NO_ERROR).withRunnedProgram(Program.MEDIUM).build();
        assertEquals(laundryReturnStatus, washingMachine.start(laundryBatch,programConfiguration));
    }
}
